package seedu.address.logic.parser.athlete;

import static seedu.address.logic.AthleteMessages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.athlete.AthleteCommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.athlete.AthleteCommandTestUtil.SPORT_DESC_BOB;
import static seedu.address.logic.parser.athlete.AthleteCommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.athlete.AthleteCommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.athlete.DeleteAthleteCommand;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.athlete.Name;
import seedu.address.model.athlete.Sport;

/**
 * Tests for {@link DeleteAthleteCommandParser}.
 */
public class DeleteAthleteCommandParserTest {

    private final DeleteAthleteCommandParser parser = new DeleteAthleteCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteAthleteCommand() {
        DeleteAthleteCommand expectedCommand = new DeleteAthleteCommand(new Name("Benson Meier"), new Sport("Pool"));
        assertParseSuccess(parser, NAME_DESC_BOB + SPORT_DESC_BOB, expectedCommand);
    }

    @Test
    public void parse_missingNamePrefix_failure() {
        String input = "Alice s/Basketball";
        assertParseFailure(parser, input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAthleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingSportPrefix_failure() {
        String input = "n/Alice Basketball";
        assertParseFailure(parser, input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAthleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingAllPrefixes_failure() {
        String input = "Alice Basketball";
        assertParseFailure(parser, input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAthleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_extraArguments_failure() {
        String input = "n/Alice s/Basketball extra/word";
        assertParseFailure(parser, input, ParserUtil.MESSAGE_ADDITIONAL_FIELD_DETECTED);
    }

    @Test
    public void parse_emptyInput_failure() {
        String input = "";
        assertParseFailure(parser, input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAthleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_whitespaceOnly_failure() {
        String input = "   ";
        assertParseFailure(parser, input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAthleteCommand.MESSAGE_USAGE));
    }
}
