package seedu.address.logic.parser.organization;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ORG;

import seedu.address.logic.commands.organization.DeleteOrganizationCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.organization.OrganizationName;

/**
 * Parses input arguments and creates a new DeleteOrganizationCommand object
 */
public class DeleteOrganizationCommandParser implements Parser<DeleteOrganizationCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteOrganizationCommand
     * and returns a DeleteOrganizationCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public DeleteOrganizationCommand parse(String args) throws ParseException {
        ParserUtil.ensureNoAdditionalPrefixes(args, PREFIX_ORG);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ORG);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ORG);

        if (!argMultimap.getValue(PREFIX_ORG).isPresent()
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, DeleteOrganizationCommand.MESSAGE_USAGE));
        }

        OrganizationName targetName = ParserUtil.parseOrganizationName(argMultimap.getValue(PREFIX_ORG).get());
        return new DeleteOrganizationCommand(targetName);
    }
}
