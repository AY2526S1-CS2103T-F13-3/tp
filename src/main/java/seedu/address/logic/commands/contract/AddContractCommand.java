package seedu.address.logic.commands.contract;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.ObservableList;
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
            + ": Adds a contract linking an athlete and an organization. "
            + "Parameters: "
            + "n/NAME "
            + "s/SPORT "
            + "o/ORG_NAME "
            + "sd/DDMMYYYY "
            + "ed/DDMMYYYY "
            + "am/AMOUNT\n"
            + "Example: " + COMMAND_WORD + " "
            + "n/Lebron James s/Basketball o/Nike sd/01012024 ed/01012025 am/50000000";

    public static final String MESSAGE_SUCCESS = "Contract created: %1$s";
    public static final String MESSAGE_DUPLICATE_CONTRACT = "Error: This contract already exists in playbook.io";
    public static final String MESSAGE_NOT_FOUND_FMT = "Error: %s not found: %s";
    public static final String MESSAGE_INVALID_DATE_RANGE =
            "Error: Start date (%s) must not be after end date (%s).";
    public static final String MESSAGE_TOTAL_AMOUNT_OVERFLOW = "Error: Adding this contract would exceed the maximum "
            + "total contract amount(9,223,372,036,854,775,807) for %s.";

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

    /**
     * Executes the command to add a contract to the model.
     * Validates that the athlete and organization exist, the date range is valid,
     * the contract doesn't already exist, and adding it won't cause overflow.
     *
     * @param model The model to which the contract should be added. Cannot be null.
     * @return A CommandResult indicating the success of the operation.
     * @throws CommandException If the athlete or organization doesn't exist, dates are invalid,
     *                          contract is a duplicate, or adding it would cause overflow.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Athlete athlete = findAthleteByNameOrThrow(model, athleteName, sport);
        Organization organization = findOrganizationByNameOrThrow(model, organizationName);

        if (startDate.toLocalDate().isAfter(endDate.toLocalDate())) {
            throw new CommandException(String.format(MESSAGE_INVALID_DATE_RANGE, startDate, endDate));
        }

        Contract toAdd = new Contract(athlete, athlete.getSport(), organization, startDate, endDate, amount);

        if (model.hasContract(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_CONTRACT);
        }

        ensureNoTotalOverflow(model, athlete, organization);

        boolean hadActiveFilters = model.hasActiveFilters();
        model.addContract(toAdd);

        String successMessage = String.format(MESSAGE_SUCCESS, ContractMessages.format(toAdd));
        if (hadActiveFilters) {
            model.clearAllFilters();
            successMessage += "\n\nYour 'find' filter was removed so you can see the new contract that has been added.";
        }

        return new CommandResult(successMessage, CommandResult.UiTab.CONTRACTS);
    }

    private Athlete findAthleteByNameOrThrow(Model model, Name name, Sport sport) throws CommandException {
        List<Athlete> list = model.getAddressBook().getAthleteList();
        Optional<Athlete> match = list.stream().filter(a -> a.getName().equals(name)
                && a.getSport().equals(sport)).findFirst();
        if (match.isEmpty()) {
            throw new CommandException(
                    String.format(MESSAGE_NOT_FOUND_FMT, "Athlete", name + " - " + sport)
            );
        }
        return match.get();
    }

    private void ensureNoTotalOverflow(
            Model model, Athlete athlete, Organization organization) throws CommandException {
        ObservableList<Contract> contracts =
                model.getContractList().getContractList();
        long contractAmount = amount.value;

        List<String> overflowTargets = new ArrayList<>();

        long athleteTotal = sumForPredicate(contracts,
                c -> c.getAthlete().isSameAthlete(athlete));
        if (wouldOverflow(athleteTotal, contractAmount)) {
            overflowTargets.add("the athlete");
        }

        long organizationTotal = sumForPredicate(contracts,
                c -> c.getOrganization().isSameOrganization(organization));
        if (wouldOverflow(organizationTotal, contractAmount)) {
            overflowTargets.add("the organization");
        }

        if (!overflowTargets.isEmpty()) {
            String targets = String.join(" and ", overflowTargets);
            throw new CommandException(String.format(MESSAGE_TOTAL_AMOUNT_OVERFLOW, targets));
        }
    }

    private long sumForPredicate(ObservableList<Contract> contracts,
                                 java.util.function.Predicate<Contract> predicate) {
        long total = 0;
        for (Contract contract : contracts) {
            if (!predicate.test(contract)) {
                continue;
            }
            long value = contract.getAmount().value;
            if (wouldOverflow(total, value)) {
                return Long.MAX_VALUE;
            }
            total += value;
        }
        return total;
    }

    private boolean wouldOverflow(long base, long addition) {
        return addition > 0 && base > Long.MAX_VALUE - addition;
    }

    private Organization findOrganizationByNameOrThrow(Model model, OrganizationName name) throws CommandException {
        List<Organization> list = model.getAddressBook().getOrganizationList();
        Optional<Organization> match = list.stream().filter(o -> o.getName().equals(name)).findFirst();
        if (match.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NOT_FOUND_FMT, "Organization", name));
        }
        return match.get();
    }

    /**
     * Returns true if both AddContractCommand objects have the same contract details.
     *
     * @param other The other object to compare with.
     * @return True if both objects are equal, false otherwise.
     */
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

    /**
     * Returns a string representation of this AddContractCommand.
     *
     * @return A formatted string containing all contract details.
     */
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
