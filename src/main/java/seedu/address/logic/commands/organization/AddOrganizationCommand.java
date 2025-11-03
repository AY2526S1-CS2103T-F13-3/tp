package seedu.address.logic.commands.organization;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ORG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.organization.Organization;

/**
 * Adds an organization to playbook.io.
 */
public class AddOrganizationCommand extends Command {

    public static final String COMMAND_WORD = "add-o";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an organization to playbook.io. "
            + "Parameters: "
            + PREFIX_ORG + "ORG_NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ORG + "Nike "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "partnerships@nike.com";

    public static final String MESSAGE_SUCCESS = "New organization added: %1$s";
    public static final String MESSAGE_DUPLICATE_ORGANIZATION =
        "Error: This organization already exists in playbook.io";

    private final Organization toAdd;

    /**
     * Creates an AddOrganizationCommand to add the specified {@code Organization}.
     */
    public AddOrganizationCommand(Organization organization) {
        requireNonNull(organization);
        toAdd = organization;
    }

    /**
     * Executes the command to add an organization to the model.
     *
     * @param model The model which the organization should be added to. Cannot be null.
     * @return A CommandResult indicating the success of the operation.
     * @throws CommandException If the organization already exists in the model.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        assert toAdd != null : "Organization to add should not be null";

        if (model.hasOrganization(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_ORGANIZATION);
        }

        boolean hadActiveFilters = model.hasActiveFilters();
        model.addOrganization(toAdd);
        assert model.hasOrganization(toAdd) : "Organization should exist in model after adding";
        if (hadActiveFilters) {
            model.clearAllFilters();
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)),
                CommandResult.UiTab.ORGANIZATIONS);
    }

    /**
     * Returns true if both AddOrganizationCommand objects add the same organization.
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
        if (!(other instanceof AddOrganizationCommand)) {
            return false;
        }

        AddOrganizationCommand otherAddCommand = (AddOrganizationCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    /**
     * Returns a string representation of this AddOrganizationCommand.
     *
     * @return A formatted string containing the organization to be added.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
