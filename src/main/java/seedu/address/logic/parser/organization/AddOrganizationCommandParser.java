package seedu.address.logic.parser.organization;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ORG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.stream.Stream;

import seedu.address.logic.commands.organization.AddOrganizationCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.organization.Organization;
import seedu.address.model.organization.OrganizationEmail;
import seedu.address.model.organization.OrganizationName;
import seedu.address.model.organization.OrganizationPhone;

/**
 * Parses input arguments and creates a new {@code AddOrganizationCommand} object.
 */
public class AddOrganizationCommandParser implements Parser<AddOrganizationCommand> {

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values
     * in the given {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses the given {@code String} of arguments in the context of the AddOrganizationCommand
     * and returns an AddOrganizationCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public AddOrganizationCommand parse(String args) throws ParseException {
        ParserUtil.ensureNoAdditionalPrefixes(args, PREFIX_ORG, PREFIX_PHONE, PREFIX_EMAIL);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ORG, PREFIX_PHONE, PREFIX_EMAIL);

        if (!arePrefixesPresent(argMultimap, PREFIX_ORG, PREFIX_PHONE, PREFIX_EMAIL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, AddOrganizationCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ORG, PREFIX_PHONE, PREFIX_EMAIL);

        OrganizationName name = ParserUtil.parseOrganizationName(argMultimap.getValue(PREFIX_ORG).get());
        OrganizationPhone phone = ParserUtil.parseOrganizationPhone(argMultimap.getValue(PREFIX_PHONE).get());
        OrganizationEmail email = ParserUtil.parseOrganizationEmail(argMultimap.getValue(PREFIX_EMAIL).get());

        Organization organization = new Organization(name, phone, email);

        return new AddOrganizationCommand(organization);
    }
}
