package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.athlete.AddAthleteCommand;
import seedu.address.logic.commands.athlete.DeleteAthleteCommand;
import seedu.address.logic.commands.contract.AddContractCommand;
import seedu.address.logic.commands.contract.DeleteContractCommand;
import seedu.address.logic.commands.organization.AddOrganizationCommand;
import seedu.address.logic.commands.organization.DeleteOrganizationCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.testutil.athlete.AthleteBuilder;
import seedu.address.testutil.athlete.AthleteUtil;
import seedu.address.testutil.contract.ContractBuilder;
import seedu.address.testutil.contract.ContractUtil;

/**
 * Tests for {@link AddressBookParser}.
 */
public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    // ============================================================
    // Athlete Commands
    // ============================================================

    @Test
    public void parseCommand_addAthlete() throws Exception {
        AddAthleteCommand command = (AddAthleteCommand)
                parser.parseCommand(AthleteUtil.getAddAthleteCommand(new AthleteBuilder().build()));
        assertTrue(command instanceof AddAthleteCommand);
    }

    @Test
    public void parseCommand_deleteAthlete() throws Exception {
        String args = " n/John Doe s/Football";
        assertTrue(parser.parseCommand(DeleteAthleteCommand.COMMAND_WORD + args) instanceof DeleteAthleteCommand);
    }

    // ============================================================
    // Organization Commands
    // ============================================================

    @Test
    public void parseCommand_addOrganization() throws Exception {
        String args = " o/Nike p/98765432 e/john.doe@nike.com";
        assertTrue(parser.parseCommand(AddOrganizationCommand.COMMAND_WORD + args)
                instanceof AddOrganizationCommand);
    }

    @Test
    public void parseCommand_deleteOrganization() throws Exception {
        String args = " o/Nike";
        assertTrue(parser.parseCommand(DeleteOrganizationCommand.COMMAND_WORD + args)
                instanceof DeleteOrganizationCommand);
    }

    // ============================================================
    // Contract Commands
    // ============================================================

    @Test
    public void parseCommand_addContract() throws Exception {
        AddContractCommand command = (AddContractCommand)
                parser.parseCommand(ContractUtil.getAddContractCommand(new ContractBuilder().build()));
        assertTrue(command instanceof AddContractCommand);
    }

    @Test
    public void parseCommand_deleteContract() throws Exception {
        String args = " n/Lionel Messi o/Inter Miami sd/01012024 ed/01012025 s/Football am/100000";
        assertTrue(parser.parseCommand(DeleteContractCommand.COMMAND_WORD + args)
                instanceof DeleteContractCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        String args = " an/ Alice";
        assertTrue(parser.parseCommand(FindCommand.COMMAND_WORD + args) instanceof FindCommand);
    }

    // ============================================================
    // Error handling
    // ============================================================

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () ->
                parser.parseCommand("unknownCommand"));
    }
}
