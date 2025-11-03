package seedu.address.logic.commands.organization;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.contract.Contract;
import seedu.address.model.organization.Organization;
import seedu.address.model.organization.OrganizationName;

/**
 * Deletes an organization identified by its name from playbook.io.
 */
public class DeleteOrganizationCommand extends Command {

    public static final String COMMAND_WORD = "delete-o";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the organization identified by its name. "
        + "Parameters: o/ORG_NAME\n"
        + "Example: " + COMMAND_WORD + " o/Nike";

    public static final String MESSAGE_DELETE_ORGANIZATION_SUCCESS = "Deleted Organization: %1$s";
    public static final String MESSAGE_ORGANIZATION_NOT_FOUND =
            "Error: Organization not found: '%1$s'";
    public static final String MESSAGE_ORGANIZATION_EXISTING_CONTRACT =
            "Error: Organization has existing contracts";

    private final OrganizationName targetName;

    /**
     * Creates a {@code DeleteOrganizationCommand} to delete the specified {@code Organization}.
     *
     * @param targetName the name of the organization to delete.
     */
    public DeleteOrganizationCommand(OrganizationName targetName) {
        requireNonNull(targetName);
        this.targetName = targetName;
    }

    /**
     * Executes the command to delete an organization from the model.
     * The organization must not have any existing contracts.
     * Name comparison is case-insensitive.
     *
     * @param model The model from which the organization should be deleted. Cannot be null.
     * @return A CommandResult indicating the success of the operation.
     * @throws CommandException If the organization does not exist or has an existing contract.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Organization> allOrganizations = model.getAddressBook().getOrganizationList();
        List<Contract> contracts = model.getContractList().getContractList();

        // Case-insensitive name comparison to comply with specification
        Organization organizationToDelete = allOrganizations.stream()
                .filter(org -> org.getName().fullOrganizationName
                        .equalsIgnoreCase(targetName.fullOrganizationName))
                .findFirst()
                .orElse(null);
        Contract existingContract = contracts.stream()
                .filter(c -> c.getOrganization().getName().fullOrganizationName
                        .equalsIgnoreCase(targetName.fullOrganizationName))
                .findFirst()
                .orElse(null);

        if (organizationToDelete == null) {
            throw new CommandException(String.format(MESSAGE_ORGANIZATION_NOT_FOUND, targetName));
        }

        if (existingContract != null) {
            throw new CommandException(MESSAGE_ORGANIZATION_EXISTING_CONTRACT);
        }

        boolean hadActiveFilters = model.hasActiveFilters();
        model.deleteOrganization(organizationToDelete);
        if (hadActiveFilters) {
            model.clearAllFilters();
        }
        return new CommandResult(String.format(
                MESSAGE_DELETE_ORGANIZATION_SUCCESS, Messages.format(organizationToDelete)),
                CommandResult.UiTab.ORGANIZATIONS);
    }

    /**
     * Returns true if both DeleteOrganizationCommand objects delete the same organization.
     *
     * @param other The other object to compare with.
     * @return True if both objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof DeleteOrganizationCommand
                && targetName.equals(((DeleteOrganizationCommand) other).targetName));
    }

    /**
     * Returns a string representation of this DeleteOrganizationCommand.
     *
     * @return A formatted string containing the name of the organization to be deleted.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetName", targetName)
                .toString();
    }
}
