package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.athlete.Email;
import seedu.address.model.organization.OrganizationContactName;
import seedu.address.model.organization.OrganizationEmail;
import seedu.address.model.organization.OrganizationName;
import seedu.address.model.organization.OrganizationPhone;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 * Each parse method validates and converts a raw {@code String} input into a strongly-typed model object.
 */
public class ParserUtil {

    /**
     * Message used when an index provided by the user is invalid.
     */
    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    // ============================================================
    // Common utility
    // ============================================================

    /**
     * Parses a one-based index string into an {@link Index}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param oneBasedIndex User-provided index string.
     * @return Parsed {@link Index} object.
     * @throws ParseException if the specified index is invalid or non-numeric.
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    // ============================================================
    // Organization parsing methods
    // ============================================================

    /**
     * Parses a {@code String name} into an {@link OrganizationName}.
     *
     * @param name Organization name string to parse.
     * @return A valid {@link OrganizationName}.
     * @throws ParseException if the input does not satisfy {@link OrganizationName#isValidName(String)}.
     */
    public static OrganizationName parseOrganizationName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!OrganizationName.isValidName(trimmedName)) {
            throw new ParseException(OrganizationName.MESSAGE_CONSTRAINTS);
        }
        return new OrganizationName(trimmedName);
    }

    /**
     * Parses a {@code String contactName} into an {@link OrganizationContactName}.
     *
     * @param contactName Contact name string to parse.
     * @return A valid {@link OrganizationContactName}.
     * @throws ParseException if the input does not satisfy {@link OrganizationContactName#isValidName(String)}.
     */
    public static OrganizationContactName parseOrganizationContactName(String contactName) throws ParseException {
        requireNonNull(contactName);
        String trimmedContactName = contactName.trim();
        if (!OrganizationContactName.isValidName(trimmedContactName)) {
            throw new ParseException(OrganizationContactName.MESSAGE_CONSTRAINTS);
        }
        return new OrganizationContactName(trimmedContactName);
    }

    /**
     * Parses a {@code String phone} into an {@link OrganizationPhone}.
     *
     * @param phone Phone string to parse.
     * @return A valid {@link OrganizationPhone}.
     * @throws ParseException if the input does not satisfy {@link OrganizationPhone#isValidPhone(String)}.
     */
    public static OrganizationPhone parseOrganizationPhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!OrganizationPhone.isValidPhone(trimmedPhone)) {
            throw new ParseException(OrganizationPhone.MESSAGE_CONSTRAINTS);
        }
        return new OrganizationPhone(trimmedPhone);
    }

    /**
     * Parses a {@code String email} into an {@link OrganizationEmail}.
     *
     * @param email Email string to parse.
     * @return A valid {@link OrganizationEmail}.
     * @throws ParseException if the input does not satisfy {@link OrganizationEmail#isValidEmail(String)}.
     */
    public static OrganizationEmail parseOrganizationEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!OrganizationEmail.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new OrganizationEmail(trimmedEmail);
    }
}
