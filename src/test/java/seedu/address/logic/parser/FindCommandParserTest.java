package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindCommand.SearchScope;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Unit tests for {@link FindCommandParser}.
 */
public class FindCommandParserTest {

    private final FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_validAthleteNameCommand_returnsFindCommand() throws Exception {
        FindCommand expected = new FindCommand(SearchScope.ATHLETE_NAME, "alice");
        assertEquals(expected, parser.parse("an/ alice"));
    }

    @Test
    public void parse_validContractOrganizationCommandWithQuotes_returnsFindCommand() throws Exception {
        FindCommand expected = new FindCommand(SearchScope.CONTRACT_ORGANIZATION, "Nike - Partners");
        assertEquals(expected, parser.parse("  co/   \"Nike - Partners\"  "));
    }

    @Test
    public void parse_missingArguments_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(""));
    }

    @Test
    public void parse_missingKeyword_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("an/   "));
    }

    @Test
    public void parse_unknownFlag_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("xx/ something"));
    }
}
