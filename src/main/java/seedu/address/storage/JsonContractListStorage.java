package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.athlete.Athlete;
import seedu.address.model.athlete.AthleteList;
import seedu.address.model.athlete.ReadOnlyAthleteList;
import seedu.address.model.contract.Contract;
import seedu.address.model.contract.ReadOnlyContractList;
import seedu.address.model.organization.Organization;
import seedu.address.model.organization.OrganizationList;
import seedu.address.model.organization.ReadOnlyOrganizationList;

/**
 * A class to access contract list data stored as a json file on the hard disk.
 */
public class JsonContractListStorage implements ContractListStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonContractListStorage.class);

    private final Path filePath;
    private final Path athletesFilePath;
    private final Path organizationsFilePath;

    /**
     * Constructs a {@code JsonContractListStorage} with the specified file path.
     *
     * @param filePath The path to the JSON file.
     */
    public JsonContractListStorage(Path filePath, Path athletesFilePath, Path organizationsFilePath) {
        this.filePath = filePath;
        this.athletesFilePath = athletesFilePath;
        this.organizationsFilePath = organizationsFilePath;
    }

    /**
     * Returns the file path of the ContractList JSON file.
     *
     * @return the file path.
     */
    @Override
    public Path getContractListFilePath() {
        return filePath;
    }

    /**
     * Reads the contract list data from the default JSON file path.
     *
     * @return An {@code Optional} containing the contract list if the file exists.
     * @throws DataLoadingException if there were errors loading the data
     *                              (e.g., illegal values or invalid JSON format).
     */
    @Override
    public Optional<ReadOnlyContractList> readContractList() throws DataLoadingException {
        return readContractList(filePath);
    }

    /**
     * Similar to {@link #readContractList()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    @Override
    public Optional<ReadOnlyContractList> readContractList(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableContractList> json = JsonUtil.readJsonFile(
                filePath, JsonSerializableContractList.class);

        Optional<JsonSerializableAthleteList> jsonAthletes = JsonUtil.readJsonFile(
                this.athletesFilePath, JsonSerializableAthleteList.class);

        Optional<JsonSerializableOrganizationList> jsonOrganizations = JsonUtil.readJsonFile(
                organizationsFilePath, JsonSerializableOrganizationList.class);

        if (!json.isPresent()) {
            return Optional.empty();
        }


        try {
            ReadOnlyContractList contractsRo = json.get().toModelType();

            ReadOnlyAthleteList athletesRo = jsonAthletes
                    .map(j -> {
                        try {
                            return j.toModelType();
                        } catch (IllegalValueException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .orElseGet(() -> new AthleteList());

            ReadOnlyOrganizationList orgsRo = jsonOrganizations
                    .map(j -> {
                        try {
                            return j.toModelType();
                        } catch (IllegalValueException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .orElseGet(() -> new OrganizationList());

            List<Athlete> athletes = new ArrayList<>(athletesRo.getAthleteList());
            List<Organization> orgs = new ArrayList<>(orgsRo.getOrganizationList());

            // Check every contract: throw if its athlete/org not found
            for (Contract c : contractsRo.getContractList()) {
                boolean athleteExists = athletes.stream().anyMatch(a ->
                    a.getName().equals(c.getAthlete().getName())
                    && a.getSport().equals(c.getAthlete().getSport())
                    && a.getPhone().equals(c.getAthlete().getPhone())
                    && a.getEmail().equals(c.getAthlete().getEmail())
                    && a.getAge().equals(c.getAthlete().getAge())
                );

                boolean orgExists = orgs.stream().anyMatch(o ->
                    o.getName().equals(c.getOrganization().getName())
                    && o.getPhone().equals(c.getOrganization().getPhone())
                    && o.getEmail().equals(c.getOrganization().getEmail())
                );

                if (!athleteExists) {
                    String msg = "Athlete does not exist for contract: " + c.toString();
                    logger.info(String.format("Data validation failed — %s (file: %s)", msg, filePath));
                    throw new DataLoadingException(new IllegalValueException(msg));
                }

                if (!orgExists) {
                    String msg = "Organization does not exist for contract: " + c.toString();
                    logger.info(String.format("Data validation failed — %s (file: %s)", msg, filePath));
                    throw new DataLoadingException(new IllegalValueException(msg));
                }
            }

            return Optional.of(contractsRo);
        } catch (IllegalValueException ive) {
            logger.info(String.format("Illegal values found in %s: %s", filePath, ive.getMessage()));
            throw new DataLoadingException(ive);
        }
    }

    /**
     * Saves the given contract list data to the default JSON file path.
     *
     * @param contracts The contract list data to save. Cannot be null.
     * @throws IOException if there was a problem writing to the file.
     */
    @Override
    public void saveContractList(ReadOnlyContractList contracts) throws IOException {
        saveContractList(contracts, filePath);
    }

    /**
     * Saves the given contract list data to the specified JSON file path.
     * Creates the file if it does not exist.
     *
     * @param contracts The contract list data to save. Cannot be null.
     * @param filePath  The location of the data file. Cannot be null.
     * @throws IOException if there was a problem writing to the file.
     */
    @Override
    public void saveContractList(ReadOnlyContractList contracts, Path filePath) throws IOException {
        requireNonNull(contracts);
        requireNonNull(filePath);
        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableContractList(contracts), filePath);
    }
}
