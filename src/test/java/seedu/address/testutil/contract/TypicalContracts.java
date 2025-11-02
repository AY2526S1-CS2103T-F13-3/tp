package seedu.address.testutil.contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.contract.Contract;
import seedu.address.testutil.OrganizationBuilder;
import seedu.address.testutil.athlete.AthleteBuilder;

/**
 * A utility class containing a list of {@code Contract} objects to be used in tests.
 */
public class TypicalContracts {

    public static final Contract MESSI_MIAMI = new ContractBuilder()
            .withAthlete(new AthleteBuilder().withName("Lionel Messi").withSport("Football")
                    .withAge("36").withEmail("messi@intermiami.com").withPhone("91234567").build())
            .withOrganization(new OrganizationBuilder().withName("Inter Miami CF").build())
            .withStartDate("01012024")
            .withEndDate("31122024")
            .withAmount(5000000L)
            .build();

    public static final Contract RONALDO_ALNASSR = new ContractBuilder()
            .withAthlete(new AthleteBuilder().withName("Cristiano Ronaldo").withSport("Football")
                    .withAge("39").withEmail("ronaldo@alnassr.com").withPhone("92345678").build())
            .withOrganization(new OrganizationBuilder().withName("Al Nassr").build())
            .withStartDate("01022024")
            .withEndDate("31012025")
            .withAmount(6000000L)
            .build();

    public static final Contract LEBRON_LAKERS = new ContractBuilder()
            .withAthlete(new AthleteBuilder().withName("LeBron James").withSport("Basketball")
                    .withAge("38").withEmail("lebron@lakers.com").withPhone("93456789").build())
            .withOrganization(new OrganizationBuilder().withName("LA Lakers").build())
            .withStartDate("01072023")
            .withEndDate("30062024")
            .withAmount(4500000L)
            .build();

    public static final Contract FEDERER_UNIQLO = new ContractBuilder()
            .withAthlete(new AthleteBuilder().withName("Roger Federer").withSport("Tennis")
                    .withAge("41").withEmail("roger@uniqlo.com").withPhone("94567890").build())
            .withOrganization(new OrganizationBuilder().withName("Uniqlo").build())
            .withStartDate("01032022")
            .withEndDate("28022025")
            .withAmount(3000000L)
            .build();

    private TypicalContracts() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical contracts.
     */
    public static AddressBook getTypicalAddressBookWithContracts() {
        AddressBook ab = new AddressBook();
        for (Contract contract : getTypicalContracts()) {
            ab.addContract(contract);
        }
        return ab;
    }

    /**
     * Returns a list of typical contracts.
     */
    public static List<Contract> getTypicalContracts() {
        return new ArrayList<>(Arrays.asList(
                MESSI_MIAMI,
                RONALDO_ALNASSR,
                LEBRON_LAKERS,
                FEDERER_UNIQLO
        ));
    }
}
