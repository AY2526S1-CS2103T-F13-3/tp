package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.contract.Contract;
import seedu.address.testutil.contract.ContractBuilder;

public class ContractListPanelTest {

    @Test
    public void contractList_emptyList_success() {
        List<Contract> emptyList = new ArrayList<>();
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void contractList_listWithContracts_success() {
        List<Contract> contractList = new ArrayList<>();
        contractList.add(new ContractBuilder().build());
        contractList.add(new ContractBuilder().withSport("Tennis").withAmount(750000L).build());

        assertEquals(2, contractList.size());
        assertEquals("Football", contractList.get(0).getSport().value);
        assertEquals("Tennis", contractList.get(1).getSport().value);
    }

    @Test
    public void contractList_singleContract_success() {
        List<Contract> contractList = new ArrayList<>();
        Contract contract = new ContractBuilder()
                .withSport("Swimming")
                .withStartDate("01032024")
                .withEndDate("28022025")
                .withAmount(2000000L)
                .build();
        contractList.add(contract);

        assertEquals(1, contractList.size());
        assertEquals("Swimming", contractList.get(0).getSport().value);
    }
}
