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
import seedu.address.model.athlete.Name;
import seedu.address.model.athlete.Sport;
import seedu.address.model.contract.Amount;
import seedu.address.model.contract.Contract;
import seedu.address.model.contract.Date8;
import seedu.address.model.organization.OrganizationName;

/**
 * Deletes an existing {@link Contract} between an athlete and organization.
 */
public class DeleteContractCommand extends Command {

    public static final String COMMAND_WORD = "delete-c";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a contract by athlete, organization, and dates. "
            + "Parameters: "
            + "n/NAME "
            + "s/SPORT "
            + "o/ORG_NAME "
            + "sd/DDMMYYYY "
            + "ed/DDMMYYYY "
            + "am/AMOUNT\n"
            + "Example: " + COMMAND_WORD + " n/Lebron James s/Basketball o/Nike sd/01012024 ed/01012025 am/50000000";

    public static final String MESSAGE_SUCCESS = "Deleted contract: %1$s";
    public static final String MESSAGE_CONTRACT_NOT_FOUND =
            "Error: No matching contract found for athlete '%s', sport '%s', organization '%s', "
            + "start '%s', end '%s', amount '%s'.";

    private final Name athleteName;
    private final OrganizationName organizationName;
    private final Date8 startDate;
    private final Date8 endDate;
    private final Sport sport;
    private final Amount amount;

    /**
     * Constructs a {@code DeleteContractCommand} with the specified athlete, organization, dates, sport, and amount.
     *
     * @param athleteName      Name of the athlete whose contract is to be deleted. Cannot be null.
     * @param organizationName Name of the organization involved in the contract. Cannot be null.
     * @param startDate        Start date of the contract. Cannot be null.
     * @param endDate          End date of the contract. Cannot be null.
     * @param sport            Sport of the athlete in the contract. Cannot be null.
     * @param amount           Amount of the contract. Cannot be null.
     */
    public DeleteContractCommand(Name athleteName, OrganizationName organizationName,
        Date8 startDate, Date8 endDate, Sport sport, Amount amount) {
        requireNonNull(athleteName);
        requireNonNull(organizationName);
        requireNonNull(startDate);
        requireNonNull(endDate);
        requireNonNull(sport);
        requireNonNull(amount);

        this.athleteName = athleteName;
        this.organizationName = organizationName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sport = sport;
        this.amount = amount;
    }

    /**
     * Executes the command to delete a contract from the model.
     * The contract is identified by matching athlete name, sport, organization name,
     * start date, end date, and amount.
     *
     * @param model The model from which the contract should be deleted. Cannot be null.
     * @return A CommandResult indicating the success of the operation.
     * @throws CommandException If no matching contract is found.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        assert athleteName != null : "Athlete name should not be null";
        assert sport != null : "Sport should not be null";
        assert organizationName != null : "Organization name should not be null";
        assert startDate != null : "Start date should not be null";
        assert endDate != null : "End date should not be null";
        assert amount != null : "Amount should not be null";

        // Search the filtered contract list (or use addressBook.getContractList())
        List<Contract> contracts = model.getContractList().getContractList();
        Optional<Contract> match = contracts.stream()
                .filter(c ->
                        c.getAthlete().getName().equals(this.athleteName)
                                && c.getOrganization().getName().equals(this.organizationName)
                                && c.getStartDate().equals(this.startDate)
                                && c.getEndDate().equals(this.endDate)
                                && c.getAthlete().getSport().equals(this.sport)
                                && c.getAmount().equals(this.amount))
                .findFirst();

        if (match.isEmpty()) {
            throw new CommandException(String.format(
                    MESSAGE_CONTRACT_NOT_FOUND,
                    athleteName, sport, organizationName, startDate, endDate, amount
            ));
        }

        Contract toDelete = match.get();
        boolean hadActiveFilters = model.hasActiveFilters();
        model.deleteContract(toDelete);
        if (hadActiveFilters) {
            model.clearAllFilters();
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, ContractMessages.format(toDelete)),
                CommandResult.UiTab.CONTRACTS);
    }

    /**
     * Returns true if both DeleteContractCommand objects have the same contract details.
     *
     * @param other The other object to compare with.
     * @return True if both objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof DeleteContractCommand)
                && athleteName.equals(((DeleteContractCommand) other).athleteName)
                && sport.equals(((DeleteContractCommand) other).sport)
                && organizationName.equals(((DeleteContractCommand) other).organizationName)
                && startDate.equals(((DeleteContractCommand) other).startDate)
                && endDate.equals(((DeleteContractCommand) other).endDate)
                && amount.equals(((DeleteContractCommand) other).amount);
    }

    /**
     * Returns a string representation of this DeleteContractCommand.
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
