package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.athlete.Name;
import seedu.address.model.athlete.Sport;
import seedu.address.model.contract.Amount;
import seedu.address.model.contract.Date8;
import seedu.address.model.organization.OrganizationName;


/**
 * Contains utility methods used for parsing strings into Contract-related model objects.
 */
public class ContractParserUtil {

    /**
     * Parses a {@code String sport} into a {@code Sport}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code sport} is invalid.
     */
    public static Sport parseSport(String sport) throws ParseException {
        requireNonNull(sport);
        String trimmed = sport.trim();
        try {
            return new Sport(trimmed); // let the constructor validate
        } catch (IllegalArgumentException e) {
            throw new ParseException(Sport.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Parses a {@code String date} in DDMMYYYY into a {@code Date8}.
     *
     * @throws ParseException if the given {@code date} is invalid.
     */
    public static Date8 parseDate8(String date) throws ParseException {
        requireNonNull(date);
        String trimmed = date.trim();
        if (!Date8.isValidDate8(trimmed)) {
            throw new ParseException(Date8.MESSAGE_CONSTRAINTS);
        }
        return new Date8(trimmed);
    }

    /**
     * Parses a {@code String amount} into an {@code Amount}.
     *
     * @throws ParseException if the given {@code amount} is invalid.
     */
    public static Amount parseAmount(String amount) throws ParseException {
        requireNonNull(amount);
        String trimmed = amount.trim();
        if (!Amount.isValidAmount(trimmed)) {
            throw new ParseException(Amount.MESSAGE_CONSTRAINTS);
        }
        return new Amount(Long.parseLong(trimmed));
    }

    // --- Convenience pass-throughs for person names (athlete/org/contact) ---

    public static Name parseAthleteName(String name) throws ParseException {
        return parseStrictName(name);
    }

    public static OrganizationName parseOrgName(String name) throws ParseException {
        return parseStrictOrganizationName(name);
    }

    private static Name parseStrictName(String name) throws ParseException {
        requireNonNull(name);
        String trimmed = name.trim();
        // letters and spaces only, at least one letter
        if (!trimmed.matches("(?i)^[A-Z]+(?:[ A-Z]+)*$")) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmed);
    }

    private static OrganizationName parseStrictOrganizationName(String name) throws ParseException {
        requireNonNull(name);
        String trimmed = name.trim();
        // letters and spaces only, at least one letter
        if (!trimmed.matches("(?i)^[A-Z]+(?:[ A-Z]+)*$")) {
            throw new ParseException(OrganizationName.MESSAGE_CONSTRAINTS);
        }
        return new OrganizationName(trimmed);
    }
}
