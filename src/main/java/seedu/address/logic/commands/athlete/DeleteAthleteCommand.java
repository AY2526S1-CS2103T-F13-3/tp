package seedu.address.logic.commands.athlete;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPORT;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.AthleteMessages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.athlete.Athlete;
import seedu.address.model.athlete.Name;
import seedu.address.model.athlete.Sport;
import seedu.address.model.contract.Contract;

/**
 * Deletes an athlete identified from playbook.io.
 */
public class DeleteAthleteCommand extends Command {

    public static final String COMMAND_WORD = "delete-a";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the athlete identified by name and sport. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_SPORT + "SPORT\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "LeBron James "
            + PREFIX_SPORT + "Basketball";

    public static final String MESSAGE_DELETE_ATHLETE_SUCCESS = "Deleted Athlete: %1$s";

    private final Name name;
    private final Sport sport;

    /**
     * Creates a {@code DeleteAthleteCommand} to delete the specified athlete.
     *
     * @param name  Name of the athlete to delete.
     * @param sport Sport of the athlete to delete.
     */
    public DeleteAthleteCommand(Name name, Sport sport) {
        this.name = name;
        this.sport = sport;
    }

    /**
     * Executes the command to delete an athlete from the model.
     * The athlete must not have any existing contracts.
     *
     * @param model The model from which the athlete should be deleted. Cannot be null.
     * @return A CommandResult indicating the success of the operation.
     * @throws CommandException If the athlete does not exist or has an existing contract.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Athlete> allAthletes = model.getAddressBook().getAthleteList();
        List<Contract> contracts = model.getContractList().getContractList();

        Athlete athleteToDelete = allAthletes.stream()
                .filter(a -> a.getName().equals(this.name)
                        && a.getSport().equals(this.sport))
                .findFirst()
                .orElse(null);

        Contract existingContract = contracts.stream()
                .filter(c -> c.getAthlete().getName().equals(this.name)
                        && c.getAthlete().getSport().equals(this.sport))
                .findFirst()
                .orElse(null);

        if (athleteToDelete == null) {
            throw new CommandException(AthleteMessages.MESSAGE_INVALID_ATHLETE + ": " + this.name + " - " + this.sport);
        }

        if (existingContract != null) {
            throw new CommandException(AthleteMessages.MESSAGE_EXISTING_CONTRACT);
        }

        boolean hadActiveFilters = model.hasActiveFilters();
        model.deleteAthlete(athleteToDelete);
        if (hadActiveFilters) {
            model.clearAllFilters();
        }
        return new CommandResult(String.format(MESSAGE_DELETE_ATHLETE_SUCCESS,
                AthleteMessages.format(athleteToDelete)), CommandResult.UiTab.ATHLETES);

    }

    /**
     * Returns true if both DeleteAthleteCommand objects delete the same athlete.
     *
     * @param other The other object to compare with.
     * @return True if both objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteAthleteCommand)) {
            return false;
        }

        DeleteAthleteCommand otherDeleteAthleteCommand = (DeleteAthleteCommand) other;
        return name.equals(otherDeleteAthleteCommand.name)
                && sport.equals(otherDeleteAthleteCommand.sport);
    }

    /**
     * Returns a string representation of this DeleteAthleteCommand.
     *
     * @return A formatted string containing the name and sport of the athlete to be deleted.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("sport", sport)
                .toString();
    }
}
