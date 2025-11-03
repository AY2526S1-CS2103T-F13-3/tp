package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.FindCommand.MESSAGE_USAGE;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindCommand.SearchScope;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.athlete.Name;
import seedu.address.model.athlete.Sport;
import seedu.address.model.organization.OrganizationName;

/**
 * Parses input arguments and creates a new {@link FindCommand} object.
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * Expected format: FLAG/ KEYWORD, where FLAG is one of: an, as, on, ca, co, cs
     *
     * @param args The user input arguments to parse. Cannot be null.
     * @return A FindCommand object with the parsed search scope and keyword.
     * @throws ParseException If the user input does not conform to the expected format,
     *                        the flag is invalid, or the keyword is blank.
     */
    @Override
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmed = args.trim();

        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    MESSAGE_USAGE));
        }

        // Regex: allow query to be empty (0 or more chars)
        Pattern pattern = Pattern.compile("^(?<flag>an|as|on|ca|co|cs)/\\s*(?<query>.*)$",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(trimmed);

        if (!matcher.matches()) {
            throw new ParseException(String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    MESSAGE_USAGE));
        }

        String flag = matcher.group("flag").toLowerCase(Locale.ENGLISH);
        String rawQuery = matcher.group("query").trim().replaceAll("\\s+", " ");

        if (rawQuery.isEmpty()) {
            throw new ParseException("Keyword to be searched cannot be blank or consists of spaces only.");
        }

        String keyword = stripEnclosingQuotes(rawQuery);

        if (keyword.isEmpty()) {
            throw new ParseException("Keyword to be searched cannot be blank or consists of spaces only.");
        }

        SearchScope scope = mapFlagToScope(flag);
        validateKeywordForScope(scope, keyword);
        return new FindCommand(scope, keyword);
    }

    private void validateKeywordForScope(SearchScope scope, String keyword) throws ParseException {
        switch (scope) {
        case ATHLETE_NAME:
        case CONTRACT_ATHLETE:
            if (!Name.isValidName(keyword)) {
                throw new ParseException(Name.MESSAGE_CONSTRAINTS);
            }
            break;
        case ATHLETE_SPORT:
        case CONTRACT_SPORT:
            if (!Sport.isValidSport(keyword)) {
                throw new ParseException(Sport.MESSAGE_CONSTRAINTS);
            }
            break;
        case ORGANIZATION_NAME:
        case CONTRACT_ORGANIZATION:
            if (!OrganizationName.isValidName(keyword)) {
                throw new ParseException(OrganizationName.MESSAGE_CONSTRAINTS);
            }
            break;
        default:
            break;
        }
    }

    private SearchScope mapFlagToScope(String flag) throws ParseException {
        switch (flag) {
        case "an":
            return SearchScope.ATHLETE_NAME;
        case "as":
            return SearchScope.ATHLETE_SPORT;
        case "on":
            return SearchScope.ORGANIZATION_NAME;
        case "ca":
            return SearchScope.CONTRACT_ATHLETE;
        case "co":
            return SearchScope.CONTRACT_ORGANIZATION;
        case "cs":
            return SearchScope.CONTRACT_SPORT;
        default:
            throw new ParseException(String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    MESSAGE_USAGE));
        }
    }

    private String stripEnclosingQuotes(String value) {
        if ((value.startsWith("\"") && value.endsWith("\""))
                || (value.startsWith("'") && value.endsWith("'"))) {
            return value.substring(1, value.length() - 1).trim();
        }
        return value;
    }
}
