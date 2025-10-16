package seedu.address.logic.commands.contract;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.ContractMessages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.athlete.Athlete;
import seedu.address.model.athlete.Name;
import seedu.address.model.athlete.Sport;
import seedu.address.model.contract.Amount;
import seedu.address.model.contract.Contract;
import seedu.address.model.contract.Date8;
import seedu.address.model.organization.Organization;
import seedu.address.model.organization.OrganizationName;

/**
 * Adds a {@link Contract} linking an athlete and organization.
 */
public class AddContractCommand extends Command {

    public static final String COMMAND_WORD = "add-c";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a contract linking an athlete and an organisation. "
            + "Parameters: "
            + "n/NAME "
            + "s/SPORT "
            + "o/ORG "
            + "sd/DDMMYYYY "
            + "ed/DDMMYYYY "
            + "am/AMOUNT\n"
            + "Example: " + COMMAND_WORD + " "
            + "n/Lionel Messi s/Football o/Inter Miami sd/01012024 ed/01012025 am/50000000";

    public static final String MESSAGE_SUCCESS = "Contract created: %1$s";
    public static final String MESSAGE_DUPLICATE_CONTRACT = "This contract already exists";
    public static final String MESSAGE_NOT_FOUND_FMT = "%s not found: %s. Ensure it exists in the address book.";
    public static final String MESSAGE_INVALID_DATE_RANGE =
            "Start date (%s) must not be after end date (%s).";

    private final Name athleteName;
    private final Sport sport;
    private final OrganizationName organizationName;
    private final Date8 startDate;
    private final Date8 endDate;
    private final Amount amount;

    /**
     * Constructs an {@code AddContractCommand} with the given details.
     *
     * @param athleteName      Name of the athlete.
     * @param sport            Sport of the contract.
     * @param organizationName Name of the organization.
     * @param startDate        Start date of the contract.
     * @param endDate          End date of the contract.
     * @param amount           Amount in the contract.
     */
    public AddContractCommand(Name athleteName,
                              Sport sport,
                              OrganizationName organizationName,
                              Date8 startDate,
                              Date8 endDate,
                              Amount amount) {
        requireNonNull(athleteName);
        requireNonNull(sport);
        requireNonNull(organizationName);
        requireNonNull(startDate);
        requireNonNull(endDate);
        requireNonNull(amount);

        this.athleteName = athleteName;
        this.sport = sport;
        this.organizationName = organizationName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Athlete athlete = findAthleteByNameOrThrow(model, athleteName);
        Organization organization = findOrganizationByNameOrThrow(model, organizationName);

        if (startDate.toLocalDate().isAfter(endDate.toLocalDate())) {
            throw new CommandException(String.format(MESSAGE_INVALID_DATE_RANGE, startDate, endDate));
        }

        Contract toAdd = new Contract(athlete, sport, organization, startDate, endDate, amount);

        if (model.hasContract(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_CONTRACT);
        }

        model.addContract(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, ContractMessages.format(toAdd)));
    }

    private Athlete findAthleteByNameOrThrow(Model model, Name name) throws CommandException {
        List<Athlete> list = model.getAddressBook().getAthleteList();
        Optional<Athlete> match = list.stream().filter(a -> a.getName().equals(name)).findFirst();
        if (match.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NOT_FOUND_FMT, "Athlete", name));
        }
        return match.get();
    }

    private Organization findOrganizationByNameOrThrow(Model model, OrganizationName name) throws CommandException {
        List<Organization> list = model.getAddressBook().getOrganizationList();
        Optional<Organization> match = list.stream().filter(o -> o.getName().equals(name)).findFirst();
        if (match.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NOT_FOUND_FMT, "Organization", name));
        }
        return match.get();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AddContractCommand)
                && athleteName.equals(((AddContractCommand) other).athleteName)
                && sport.equals(((AddContractCommand) other).sport)
                && organizationName.equals(((AddContractCommand) other).organizationName)
                && startDate.equals(((AddContractCommand) other).startDate)
                && endDate.equals(((AddContractCommand) other).endDate)
                && amount.equals(((AddContractCommand) other).amount);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("athleteName", athleteName)
                .add("sport", sport)
                .add("organizationName", organizationName)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("amount", amount)
                .toString();
    }
}
