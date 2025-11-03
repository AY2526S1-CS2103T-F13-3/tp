package seedu.address.logic.commands.athlete;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPORT;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.AthleteMessages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.athlete.Athlete;


/**
 * Adds an athlete to playbook.io.
 */
public class AddAthleteCommand extends Command {

    public static final String COMMAND_WORD = "add-a";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an athlete to playbook.io. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_SPORT + "SPORT "
            + PREFIX_AGE + "AGE "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "LeBron James "
            + PREFIX_SPORT + "Basketball "
            + PREFIX_AGE + "40 "
            + PREFIX_PHONE + "99876543 "
            + PREFIX_EMAIL + "James@example.com ";

    public static final String MESSAGE_SUCCESS = "New athlete added: %1$s";
    public static final String MESSAGE_DUPLICATE_ATHLETE = "Error: This athlete already exists in playbook.io";

    private final Athlete toAdd;

    /**
     * Creates an AddAthleteCommand to add the specified {@code Athlete}
     */
    public AddAthleteCommand(Athlete athlete) {
        requireNonNull(athlete);
        toAdd = athlete;
    }

    /**
     * Executes the command to add an athlete to the model.
     *
     * @param model The model which the athlete should be added to. Cannot be null.
     * @return A CommandResult indicating the success of the operation.
     * @throws CommandException If the athlete already exists in the model.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        assert toAdd != null : "Athlete to add should not be null";

        if (model.hasAthlete(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_ATHLETE);
        }

        boolean hadActiveFilters = model.hasActiveFilters();
        model.addAthlete(toAdd);
        assert model.hasAthlete(toAdd) : "Athlete should exist in model after adding";
        if (hadActiveFilters) {
            model.clearAllFilters();
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, AthleteMessages.format(toAdd)),
                CommandResult.UiTab.ATHLETES);
    }

    /**
     * Returns true if both AddAthleteCommand objects add the same athlete.
     *
     * @param other The other object to compare with.
     * @return True if both objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddAthleteCommand)) {
            return false;
        }

        AddAthleteCommand otherAddAthleteCommand = (AddAthleteCommand) other;
        return toAdd.equals(otherAddAthleteCommand.toAdd);
    }

    /**
     * Returns a string representation of this AddAthleteCommand.
     *
     * @return A formatted string containing the athlete to be added.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
