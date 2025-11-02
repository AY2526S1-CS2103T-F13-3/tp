package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import seedu.address.model.contract.Contract;
import seedu.address.testutil.contract.ContractBuilder;

public class ContractCardTest {

    @Test
    public void contractCard_validContract_fieldsCorrect() {
        Contract contract = new ContractBuilder().build();
        assertNotNull(contract);
        assertEquals("Football", contract.getSport().value);
        assertEquals("01012024", contract.getStartDate().value);
        assertEquals("31122024", contract.getEndDate().value);
        assertEquals(5000000, contract.getAmount().value);
    }

    @Test
    public void contractCard_customData_fieldsCorrect() {
        Contract contract = new ContractBuilder()
                .withSport("Basketball")
                .withStartDate("15062024")
                .withEndDate("14062025")
                .withAmount(1000000L)
                .build();

        assertNotNull(contract);
        assertEquals("Basketball", contract.getSport().value);
        assertEquals("15062024", contract.getStartDate().value);
        assertEquals("14062025", contract.getEndDate().value);
        assertEquals(1000000, contract.getAmount().value);
    }
}
