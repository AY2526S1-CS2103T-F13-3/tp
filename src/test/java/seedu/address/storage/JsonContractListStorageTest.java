package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.contract.TypicalContracts.MESSI_MIAMI;
import static seedu.address.testutil.contract.TypicalContracts.getTypicalContracts;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.contract.Contract;
import seedu.address.model.contract.ContractList;
import seedu.address.model.contract.ReadOnlyContractList;
import seedu.address.testutil.OrganizationBuilder;
import seedu.address.testutil.athlete.AthleteBuilder;
import seedu.address.testutil.contract.ContractBuilder;

public class JsonContractListStorageTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data",
            "JsonContractListStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readContractList_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readContractList(null));
    }

    private Optional<ReadOnlyContractList> readContractList(String filePath) throws Exception {
        return new JsonContractListStorage(Paths.get(filePath))
                .readContractList(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readContractList("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readContractList("notJsonFormatContractList.json"));
    }

    @Test
    public void readContractList_invalidContractContractList_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readContractList("invalidContractContractList.json"));
    }

    @Test
    public void readContractList_invalidAndValidContractContractList_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readContractList(
                "invalidAndValidContractContractList.json"));
    }

    @Test
    public void readAndSaveContractList_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempContractList.json");
        ContractList original = new ContractList();
        original.setContracts(getTypicalContracts());
        JsonContractListStorage jsonContractListStorage = new JsonContractListStorage(filePath);

        // Save in new file and read back
        jsonContractListStorage.saveContractList(original, filePath);
        ReadOnlyContractList readBack = jsonContractListStorage.readContractList(filePath).get();
        assertEquals(original.getContractList(), readBack.getContractList());

        // Modify data, overwrite exiting file, and read back
        Contract newContract = new ContractBuilder()
                .withAthlete(new AthleteBuilder().withName("Serena Williams").withSport("Tennis")
                        .withAge("42").withEmail("serena@nike.com").withPhone("96789012").build())
                .withOrganization(new OrganizationBuilder().withName("Nike Sports").build())
                .withStartDate("01012025")
                .withEndDate("31122025")
                .withAmount(7000000L)
                .build();
        original.addContract(newContract);
        original.removeContract(MESSI_MIAMI);
        jsonContractListStorage.saveContractList(original, filePath);
        readBack = jsonContractListStorage.readContractList(filePath).get();
        assertEquals(original.getContractList(), readBack.getContractList());

        // Save and read without specifying file path
        original.addContract(MESSI_MIAMI);
        jsonContractListStorage.saveContractList(original); // file path not specified
        readBack = jsonContractListStorage.readContractList().get(); // file path not specified
        assertEquals(original.getContractList(), readBack.getContractList());
    }

    @Test
    public void saveContractList_nullContractList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveContractList(null, "SomeFile.json"));
    }

    /**
     * Saves {@code contractList} at the specified {@code filePath}.
     */
    private void saveContractList(ReadOnlyContractList contractList, String filePath) {
        try {
            new JsonContractListStorage(Paths.get(filePath))
                    .saveContractList(contractList, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveContractList_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveContractList(new ContractList(), null));
    }
}
