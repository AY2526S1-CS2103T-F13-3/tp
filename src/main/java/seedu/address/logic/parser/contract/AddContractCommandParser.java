package seedu.address.logic.parser.contract;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ORG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPORT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;

import seedu.address.logic.commands.contract.AddContractCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.ContractParserUtil;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.athlete.Name;
import seedu.address.model.athlete.Sport;
import seedu.address.model.contract.Amount;
import seedu.address.model.contract.Date8;
import seedu.address.model.organization.OrganizationName;


/**
 * Parses input arguments and creates a new AddContractCommand object.
 * This parser builds value objects and the command will resolve the corresponding
 * Athlete and Organization objects during execution.
 */
public class AddContractCommandParser implements Parser<AddContractCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddContractCommand
     * and returns an AddContractCommand object for execution.
     *
     * @param args The user input arguments to parse.
     * @return An AddContractCommand object with the parsed contract details.
     * @throws ParseException If the user input does not conform to the expected format,
     *                        contains duplicate prefixes, or has invalid field values.
     */
    @Override
    public AddContractCommand parse(String args) throws ParseException {
        ParserUtil.ensureNoAdditionalPrefixes(args,
                PREFIX_NAME, PREFIX_SPORT, PREFIX_ORG, PREFIX_START_DATE, PREFIX_END_DATE, PREFIX_AMOUNT);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args,
                PREFIX_NAME,
                PREFIX_SPORT,
                PREFIX_ORG,
                PREFIX_START_DATE,
                PREFIX_END_DATE,
                PREFIX_AMOUNT
        );

        if (!arePrefixesPresent(argMultimap,
                PREFIX_NAME, PREFIX_SPORT, PREFIX_ORG,
                PREFIX_START_DATE, PREFIX_END_DATE, PREFIX_AMOUNT)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddContractCommand.MESSAGE_USAGE));
        }

        // Disallow duplicate fields
        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_NAME, PREFIX_SPORT, PREFIX_ORG,
                PREFIX_START_DATE, PREFIX_END_DATE, PREFIX_AMOUNT
        );

        // Parse values with the proper utilities
        Name athleteName = ContractParserUtil.parseAthleteName(argMultimap.getValue(PREFIX_NAME).get());
        Sport sport = ContractParserUtil.parseSport(argMultimap.getValue(PREFIX_SPORT).get());
        OrganizationName organizationName = ContractParserUtil.parseOrgName(argMultimap.getValue(PREFIX_ORG).get());
        Date8 startDate = ContractParserUtil.parseDate8(argMultimap.getValue(PREFIX_START_DATE).get());
        Date8 endDate = ContractParserUtil.parseDate8(argMultimap.getValue(PREFIX_END_DATE).get());
        Amount amount = ContractParserUtil.parseAmount(argMultimap.getValue(PREFIX_AMOUNT).get());

        // Build command (Person resolution happens in AddContractCommand#execute)
        return new AddContractCommand(athleteName, sport, organizationName, startDate, endDate, amount);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap mm, Prefix... prefixes) {
        return java.util.stream.Stream.of(prefixes).allMatch(p -> !mm.getAllValues(p).isEmpty());
    }
}
