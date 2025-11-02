package seedu.address.logic.parser.organization;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ORG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.organization.AddOrganizationCommand;
import seedu.address.model.athlete.Email;
import seedu.address.model.organization.Organization;
import seedu.address.model.organization.OrganizationName;
import seedu.address.model.organization.OrganizationPhone;
import seedu.address.testutil.OrganizationBuilder;

public class AddOrganizationCommandParserTest {

    private static final String VALID_ORG_NAME = "Nike";
    private static final String VALID_PHONE = "98765432";
    private static final String VALID_EMAIL = "john@nike.com";
    private static final String ORG_DESC = " " + PREFIX_ORG + VALID_ORG_NAME;
    private static final String PHONE_DESC = " " + PREFIX_PHONE + VALID_PHONE;
    private static final String EMAIL_DESC = " " + PREFIX_EMAIL + VALID_EMAIL;

    private static final String INVALID_ORG_DESC = " " + PREFIX_ORG + "@@@"; // invalid symbols
    private static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "12"; // less than 8 digits
    private static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "abc@.com"; // invalid email

    private final AddOrganizationCommandParser parser = new AddOrganizationCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Organization expectedOrg = new OrganizationBuilder()
                .withName(VALID_ORG_NAME)
                .withPhone(VALID_PHONE)
                .withEmail(VALID_EMAIL)
                .build();

        assertParseSuccess(parser,
                ORG_DESC + PHONE_DESC + EMAIL_DESC,
                new AddOrganizationCommand(expectedOrg));
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddOrganizationCommand.MESSAGE_USAGE);

        // missing organization prefix
        assertParseFailure(parser,
                VALID_ORG_NAME + PHONE_DESC + EMAIL_DESC,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser,
                ORG_DESC + VALID_PHONE + EMAIL_DESC,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser,
                ORG_DESC + PHONE_DESC + VALID_EMAIL,
                expectedMessage);

        // all missing
        assertParseFailure(parser,
                VALID_ORG_NAME + VALID_PHONE + VALID_EMAIL,
                expectedMessage);
    }

    @Test
    public void parse_invalidValues_failure() {
        // invalid organization name
        assertParseFailure(parser,
                INVALID_ORG_DESC + PHONE_DESC + EMAIL_DESC,
                OrganizationName.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser,
                ORG_DESC + INVALID_PHONE_DESC + EMAIL_DESC,
                OrganizationPhone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser,
                ORG_DESC + PHONE_DESC + INVALID_EMAIL_DESC,
                Email.MESSAGE_CONSTRAINTS);

        // multiple invalids — only first reported
        assertParseFailure(parser,
                INVALID_ORG_DESC + INVALID_PHONE_DESC + INVALID_EMAIL_DESC,
                OrganizationName.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        // multiple organization names
        assertParseFailure(parser,
                ORG_DESC + ORG_DESC + PHONE_DESC + EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ORG));

        // multiple phones
        assertParseFailure(parser,
                ORG_DESC + PHONE_DESC + PHONE_DESC + EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser,
                ORG_DESC + PHONE_DESC + EMAIL_DESC + EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple across fields
        assertParseFailure(parser,
                ORG_DESC + PHONE_DESC + EMAIL_DESC
                        + ORG_DESC + PHONE_DESC + EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ORG, PREFIX_PHONE, PREFIX_EMAIL));
    }
}
