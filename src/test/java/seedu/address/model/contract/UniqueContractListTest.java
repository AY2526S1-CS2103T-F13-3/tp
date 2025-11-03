package seedu.address.model.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.model.contract.exceptions.ContractNotFoundException;
import seedu.address.model.contract.exceptions.DuplicateContractException;
import seedu.address.testutil.contract.ContractBuilder;

class UniqueContractListTest {

    @Test
    void contains_nullContract_throwsNullPointerException() {
        UniqueContractList list = new UniqueContractList();
        assertThrows(NullPointerException.class, () -> list.contains(null));
    }

    @Test
    void contains_contractNotInList_returnsFalse() {
        UniqueContractList list = new UniqueContractList();
        Contract c = new ContractBuilder().build();
        assertFalse(list.contains(c));
    }

    @Test
    void contains_contractInList_returnsTrue() {
        UniqueContractList list = new UniqueContractList();
        Contract c = new ContractBuilder().build();
        list.add(c);
        assertTrue(list.contains(c));

        // An equivalent contract by identity should be considered contained
        Contract sameIdentity = new ContractBuilder(c).build();
        assertTrue(list.contains(sameIdentity));
    }

    @Test
    void add_nullContract_throwsNullPointerException() {
        UniqueContractList list = new UniqueContractList();
        assertThrows(NullPointerException.class, () -> list.add(null));
    }

    @Test
    void add_duplicate_throwsDuplicateContractException() {
        UniqueContractList list = new UniqueContractList();
        Contract c = new ContractBuilder().build();
        list.add(c);
        assertThrows(DuplicateContractException.class, () -> list.add(new ContractBuilder(c).build()));
    }

    @Test
    void setContract_nullTarget_throwsNullPointerException() {
        UniqueContractList list = new UniqueContractList();
        Contract edited = new ContractBuilder().build();
        assertThrows(NullPointerException.class, () -> list.setContract(null, edited));
    }

    @Test
    void setContract_targetNotInList_throwsContractNotFoundException() {
        UniqueContractList list = new UniqueContractList();
        Contract target = new ContractBuilder().build();
        Contract edited = new ContractBuilder(target).withAmount(999L).build();
        assertThrows(ContractNotFoundException.class, () -> list.setContract(target, edited));
    }

    @Test
    void setContract_duplicateIdentity_throwsDuplicateContractException() {
        UniqueContractList list = new UniqueContractList();
        Contract c1 = new ContractBuilder().withAmount(100L).build();
        Contract c2 = new ContractBuilder().withAmount(200L).build();
        list.add(c1);
        list.add(c2);

        // Try to change c1 to be identical in identity to c2 (all identity fields must match)
        Contract editedToC2Identity = new ContractBuilder(c2).build();
        assertThrows(DuplicateContractException.class, () -> list.setContract(c1, editedToC2Identity));
    }

    @Test
    void setContract_success_replaces() {
        UniqueContractList list = new UniqueContractList();
        Contract original = new ContractBuilder().withAmount(100L).build();
        list.add(original);

        Contract edited = new ContractBuilder(original).withAmount(200L).build();
        list.setContract(original, edited);

        assertTrue(list.contains(edited));
        assertFalse(list.contains(original)); // equals-based removal, identity changed
    }

    @Test
    void remove_nullContract_throwsNullPointerException() {
        UniqueContractList list = new UniqueContractList();
        assertThrows(NullPointerException.class, () -> list.remove(null));
    }

    @Test
    void remove_notInList_throwsContractNotFoundException() {
        UniqueContractList list = new UniqueContractList();
        Contract c = new ContractBuilder().build();
        assertThrows(ContractNotFoundException.class, () -> list.remove(c));
    }

    @Test
    void remove_inList_success() {
        UniqueContractList list = new UniqueContractList();
        Contract c = new ContractBuilder().build();
        list.add(c);
        list.remove(c);
        assertFalse(list.contains(c));
    }

    @Test
    void setContracts_uniqueList_success() {
        UniqueContractList list = new UniqueContractList();
        Contract c1 = new ContractBuilder().withAmount(100L).build();
        Contract c2 = new ContractBuilder().withAmount(200L).build();
        list.setContracts(Arrays.asList(c1, c2));
        assertTrue(list.contains(c1));
        assertTrue(list.contains(c2));
    }

    @Test
    void setContracts_withDuplicates_throwsDuplicateContractException() {
        UniqueContractList list = new UniqueContractList();
        Contract c = new ContractBuilder().build();
        List<Contract> withDupes = Arrays.asList(c, new ContractBuilder(c).build());
        assertThrows(DuplicateContractException.class, () -> list.setContracts(withDupes));
    }

    @Test
    void asUnmodifiableObservableList_modification_throwsUnsupportedOperationException() {
        UniqueContractList list = new UniqueContractList();
        ObservableList<Contract> view = list.asUnmodifiableObservableList();
        assertThrows(UnsupportedOperationException.class, () -> view.remove(0));
    }

    @Test
    void equals_hashCode() {
        UniqueContractList a = new UniqueContractList();
        UniqueContractList b = new UniqueContractList();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        Contract c = new ContractBuilder().build();
        a.add(c);
        assertNotEquals(a, b);
    }

    @Test
    void toString_ok() {
        UniqueContractList list = new UniqueContractList();
        assertNotNull(list.toString());
    }
}
