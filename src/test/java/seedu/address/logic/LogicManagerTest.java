package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.organization.AddOrganizationCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.athlete.AthleteList;
import seedu.address.model.athlete.ReadOnlyAthleteList;
import seedu.address.model.contract.ContractList;
import seedu.address.model.contract.ReadOnlyContractList;
import seedu.address.model.organization.Organization;
import seedu.address.model.organization.OrganizationEmail;
import seedu.address.model.organization.OrganizationList;
import seedu.address.model.organization.OrganizationName;
import seedu.address.model.organization.OrganizationPhone;
import seedu.address.model.organization.ReadOnlyOrganizationList;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonAthleteListStorage;
import seedu.address.storage.JsonContractListStorage;
import seedu.address.storage.JsonOrganizationListStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;

public class LogicManagerTest {
    private static final String VALID_ORG_NAME = "Acme Sports";
    private static final String VALID_ORG_PHONE = "99998888";
    private static final String VALID_ORG_EMAIL = "contact@acme.com";
    private static final String VALID_ADD_ORG_COMMAND =
            "add-o o/" + VALID_ORG_NAME + " p/" + VALID_ORG_PHONE + " e/" + VALID_ORG_EMAIL;

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        JsonAthleteListStorage athleteListStorage =
                new JsonAthleteListStorage(temporaryFolder.resolve("athletelist.json"));
        JsonContractListStorage contractListStorage =
                new JsonContractListStorage(temporaryFolder.resolve("contractlist.json"),
                    temporaryFolder.resolve("athletelist.json"), temporaryFolder.resolve("organizationlist.json"));
        JsonOrganizationListStorage organizationListStorage =
                new JsonOrganizationListStorage(temporaryFolder.resolve("organizationlist.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage,
                athleteListStorage, contractListStorage, organizationListStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_validAddOrganizationCommand_success() throws Exception {
        Organization expectedOrganization = buildOrganization(VALID_ORG_NAME, VALID_ORG_PHONE, VALID_ORG_EMAIL);
        Model expectedModel = new ModelManager();
        expectedModel.addOrganization(expectedOrganization);

        String expectedMessage = String.format(AddOrganizationCommand.MESSAGE_SUCCESS,
                Messages.format(expectedOrganization));

        assertCommandSuccess(VALID_ADD_ORG_COMMAND, expectedMessage, expectedModel);
        assertTrue(model.getFilteredOrganizationList().contains(expectedOrganization));
    }

    @Test
    public void execute_storageAccessDenied_throwsCommandException() {
        Path blockedPath = Paths.get("blocked.json");
        logic = new LogicManager(model, new AccessDeniedStorageStub(blockedPath));

        CommandException exception = org.junit.jupiter.api.Assertions.assertThrows(CommandException.class, () ->
                logic.execute(VALID_ADD_ORG_COMMAND));

        String expectedMessage = String.format(LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT,
                blockedPath.toString());
        assertEquals(expectedMessage, exception.getMessage());
        assertTrue(exception.getCause() instanceof AccessDeniedException);
        assertTrue(model.getFilteredOrganizationList().contains(
                buildOrganization(VALID_ORG_NAME, VALID_ORG_PHONE, VALID_ORG_EMAIL)));
    }

    @Test
    public void execute_storageIoException_throwsCommandException() {
        IOException ioException = new IOException("disk full");
        logic = new LogicManager(model, new IoExceptionStorageStub(ioException));

        CommandException exception = org.junit.jupiter.api.Assertions.assertThrows(CommandException.class, () ->
                logic.execute(VALID_ADD_ORG_COMMAND));

        String expectedMessage = String.format(LogicManager.FILE_OPS_ERROR_FORMAT, ioException.getMessage());
        assertEquals(expectedMessage, exception.getMessage());
        assertTrue(exception.getCause() instanceof IOException);
        assertTrue(model.getFilteredOrganizationList().contains(
                buildOrganization(VALID_ORG_NAME, VALID_ORG_PHONE, VALID_ORG_EMAIL)));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(),
                new AthleteList(), new ContractList(), new OrganizationList());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    private Organization buildOrganization(String name, String phone, String email) {
        return new Organization(new OrganizationName(name), new OrganizationPhone(phone),
                new OrganizationEmail(email));
    }

    private static class BaseStorageStub implements Storage {
        private final Path dummyPath = Paths.get("dummy.json");

        @Override
        public Optional<UserPrefs> readUserPrefs() {
            return Optional.of(new UserPrefs());
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) {
            // no-op for tests
        }

        @Override
        public Path getUserPrefsFilePath() {
            return dummyPath;
        }

        @Override
        public Path getAddressBookFilePath() {
            return dummyPath;
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook() {
            return Optional.of(new AddressBook());
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) {
            return readAddressBook();
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
            // default no-op
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
            // default no-op
        }

        @Override
        public Path getAthleteListFilePath() {
            return dummyPath;
        }

        @Override
        public Optional<ReadOnlyAthleteList> readAthleteList() throws DataLoadingException {
            return Optional.of(new AthleteList());
        }

        @Override
        public Optional<ReadOnlyAthleteList> readAthleteList(Path filePath) throws DataLoadingException {
            return readAthleteList();
        }

        @Override
        public void saveAthleteList(ReadOnlyAthleteList athletes) throws IOException {
            // default no-op
        }

        @Override
        public void saveAthleteList(ReadOnlyAthleteList athletes, Path filePath) throws IOException {
            // default no-op
        }

        @Override
        public Path getContractListFilePath() {
            return dummyPath;
        }

        @Override
        public Optional<ReadOnlyContractList> readContractList() throws DataLoadingException {
            return Optional.of(new ContractList());
        }

        @Override
        public Optional<ReadOnlyContractList> readContractList(Path filePath) throws DataLoadingException {
            return readContractList();
        }

        @Override
        public void saveContractList(ReadOnlyContractList contracts) throws IOException {
            // default no-op
        }

        @Override
        public void saveContractList(ReadOnlyContractList contracts, Path filePath) throws IOException {
            // default no-op
        }

        @Override
        public Path getOrganizationListFilePath() {
            return dummyPath;
        }

        @Override
        public Optional<ReadOnlyOrganizationList> readOrganizationList() throws DataLoadingException {
            return Optional.of(new OrganizationList());
        }

        @Override
        public Optional<ReadOnlyOrganizationList> readOrganizationList(Path filePath) throws DataLoadingException {
            return readOrganizationList();
        }

        @Override
        public void saveOrganizationList(ReadOnlyOrganizationList organizations) throws IOException {
            // default no-op
        }

        @Override
        public void saveOrganizationList(ReadOnlyOrganizationList organizations, Path filePath) throws IOException {
            // default no-op
        }
    }

    private static class AccessDeniedStorageStub extends BaseStorageStub {
        private final Path blockedPath;

        private AccessDeniedStorageStub(Path blockedPath) {
            this.blockedPath = blockedPath;
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
            throw new AccessDeniedException(blockedPath.toString());
        }
    }

    private static class IoExceptionStorageStub extends BaseStorageStub {
        private final IOException exceptionToThrow;

        private IoExceptionStorageStub(IOException exceptionToThrow) {
            this.exceptionToThrow = exceptionToThrow;
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
            throw exceptionToThrow;
        }
    }
}
