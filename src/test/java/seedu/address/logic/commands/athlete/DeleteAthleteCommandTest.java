package seedu.address.logic.commands.athlete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.athlete.AthleteCommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.athlete.AthleteCommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.athlete.TypicalAthletes.ALICE;
import static seedu.address.testutil.athlete.TypicalAthletes.getTypicalAddressBookWithAthletes;

import org.junit.jupiter.api.Test;

import seedu.address.logic.AthleteMessages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.athlete.AthleteList;
import seedu.address.model.athlete.Name;
import seedu.address.model.athlete.Sport;
import seedu.address.model.contract.ContractList;
import seedu.address.model.organization.OrganizationList;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteAthleteCommand}.
 */
public class DeleteAthleteCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBookWithAthletes(), new UserPrefs(),
            new AthleteList(), new ContractList(), new OrganizationList());

    @Test
    public void execute_validNameSportUnfilteredList_success() {
        Name nameToDelete = ALICE.getName();
        Sport sportToDelete = ALICE.getSport();

        DeleteAthleteCommand deleteCommand = new DeleteAthleteCommand(nameToDelete, sportToDelete);

        String expectedMessage = String.format(DeleteAthleteCommand.MESSAGE_DELETE_ATHLETE_SUCCESS,
                AthleteMessages.format(ALICE));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(),
                new AthleteList(), new ContractList(), new OrganizationList());
        expectedModel.deleteAthlete(ALICE);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, CommandResult.UiTab.ATHLETES);

        assertCommandSuccess(deleteCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidNameSportUnfilteredList_throwsCommandException() {
        Name invalidName = new Name("Nonexistent Athlete");
        Sport invalidSport = new Sport("Nonexistent Sport");

        DeleteAthleteCommand deleteCommand = new DeleteAthleteCommand(invalidName, invalidSport);

        assertCommandFailure(deleteCommand, model,
                AthleteMessages.MESSAGE_INVALID_ATHLETE + ": Nonexistent Athlete - Nonexistent Sport");
    }

    @Test
    public void equals() {
        DeleteAthleteCommand deleteFirstCommand = new DeleteAthleteCommand(ALICE.getName(), ALICE.getSport());
        DeleteAthleteCommand deleteSecondCommand = new DeleteAthleteCommand(new Name("Bob"), new Sport("Basketball"));

        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        DeleteAthleteCommand deleteFirstCommandCopy =
                new DeleteAthleteCommand(ALICE.getName(), ALICE.getSport());
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        assertFalse(deleteFirstCommand.equals(1));
        assertFalse(deleteFirstCommand.equals(null));
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        DeleteAthleteCommand deleteCommand = new DeleteAthleteCommand(ALICE.getName(), ALICE.getSport());
        String expected = DeleteAthleteCommand.class.getCanonicalName()
                + "{name=" + ALICE.getName()
                + ", sport=" + ALICE.getSport() + "}";
        assertEquals(expected, deleteCommand.toString());
    }
}
