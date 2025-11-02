package seedu.address.logic.parser.athlete;

import static seedu.address.logic.AthleteMessages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPORT;

import seedu.address.logic.commands.athlete.DeleteAthleteCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.AthleteParserUtil;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.athlete.Name;
import seedu.address.model.athlete.Sport;

/**
 * Parses input arguments and creates a new DeleteAthleteCommand object
 */
public class DeleteAthleteCommandParser implements Parser<DeleteAthleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteAthleteCommand
     * and returns a DeleteAthleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteAthleteCommand parse(String args) throws ParseException {
        ParserUtil.ensureNoAdditionalPrefixes(args, PREFIX_NAME, PREFIX_SPORT);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_SPORT);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_SPORT);

        if (argMultimap.getValue(PREFIX_NAME).isEmpty()
                || argMultimap.getValue(PREFIX_SPORT).isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAthleteCommand.MESSAGE_USAGE));
        }

        Name name = AthleteParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Sport sport = AthleteParserUtil.parseSport(argMultimap.getValue(PREFIX_SPORT).get());

        return new DeleteAthleteCommand(name, sport);
    }

}
