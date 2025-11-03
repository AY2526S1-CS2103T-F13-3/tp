package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.contract.ContractList;
import seedu.address.model.contract.ReadOnlyContractList;

public class JsonContractListStorageTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data",
            "JsonContractListStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readContractList_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readContractList(null, "", ""));
    }

    private Optional<ReadOnlyContractList> readContractList(String filePath,
        String athletesFilePath, String organizationsFilePath) throws Exception {
        return new JsonContractListStorage(Paths.get(filePath),
                    Paths.get(athletesFilePath), Paths.get(organizationsFilePath))
                .readContractList(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readContractList("NonExistentFile.json",
            "NonExistentFile.json", "NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () ->
            readContractList("notJsonFormatContractList.json",
                "NonExistentFile.json", "NonExistentFile.json"));
    }

    @Test
    public void readContractList_invalidContractContractList_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readContractList("invalidContractContractList.json",
            "NonExistentFile.json", "NonExistentFile.json"));
    }

    @Test
    public void readContractList_invalidAndValidContractContractList_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readContractList(
                "invalidAndValidContractContractList.json",
                "NonExistentFile.json", "NonExistentFile.json"));
    }

    @Test
    public void saveContractList_nullContractList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveContractList(null,
            "SomeFile.json", "SomeFile.json", "SomeFile.json"));
    }

    /**
     * Saves {@code contractList} at the specified {@code filePath}.
     */
    private void saveContractList(ReadOnlyContractList contractList,
        String filePath, String athleteFilePath, String organizationFilePath) {
        try {
            new JsonContractListStorage(Paths.get(filePath),
                Paths.get(athleteFilePath), Paths.get(organizationFilePath))
                    .saveContractList(contractList, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveContractList_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveContractList(new ContractList(),
            null, "SomeFile.json", "SomeFile.json"));
    }
}
