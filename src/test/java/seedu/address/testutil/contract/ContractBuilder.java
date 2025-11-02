package seedu.address.testutil.contract;

import seedu.address.model.athlete.Athlete;
import seedu.address.model.athlete.Sport;
import seedu.address.model.contract.Amount;
import seedu.address.model.contract.Contract;
import seedu.address.model.contract.Date8;
import seedu.address.model.organization.Organization;
import seedu.address.testutil.OrganizationBuilder;
import seedu.address.testutil.athlete.AthleteBuilder;

/**
 * A utility class to help with building Contract objects for testing.
 */
public class ContractBuilder {

    public static final String DEFAULT_SPORT = "Football";
    public static final String DEFAULT_START_DATE = "01012024";
    public static final String DEFAULT_END_DATE = "31122024";
    public static final String DEFAULT_AMOUNT = "5000000";

    private Athlete athlete;
    private Sport sport;
    private Organization organization;
    private Date8 startDate;
    private Date8 endDate;
    private Amount amount;

    /**
     * Creates a {@code ContractBuilder} with default fields.
     */
    public ContractBuilder() {
        this.athlete = new AthleteBuilder().build();
        this.sport = new Sport(DEFAULT_SPORT);
        this.organization = new OrganizationBuilder().build();
        this.startDate = new Date8(DEFAULT_START_DATE);
        this.endDate = new Date8(DEFAULT_END_DATE);
        this.amount = new Amount(Long.parseLong(DEFAULT_AMOUNT));
    }

    /**
     * Initializes the ContractBuilder with the data of {@code contractToCopy}.
     */
    public ContractBuilder(Contract contractToCopy) {
        this.athlete = contractToCopy.getAthlete();
        this.sport = contractToCopy.getSport();
        this.organization = contractToCopy.getOrganization();
        this.startDate = contractToCopy.getStartDate();
        this.endDate = contractToCopy.getEndDate();
        this.amount = contractToCopy.getAmount();
    }

    /**
     * Sets the athlete of the contract.
     *
     * @param athlete the athlete to associate with the contract
     * @return this builder instance for chaining
     */
    public ContractBuilder withAthlete(Athlete athlete) {
        this.athlete = athlete;
        return this;
    }

    /**
     * Sets the sport of the contract.
     *
     * @param sport the sport to associate with the contract
     * @return this builder instance for chaining
     */
    public ContractBuilder withSport(String sport) {
        this.sport = new Sport(sport);
        return this;
    }

    /**
     * Sets the organization of the contract.
     *
     * @param organization the organization to associate with the contract
     * @return this builder instance for chaining
     */
    public ContractBuilder withOrganization(Organization organization) {
        this.organization = organization;
        return this;
    }

    /**
     * Sets the start date of the contract.
     *
     * @param date the start date in ddMMyyyy format
     * @return this builder instance for chaining
     */
    public ContractBuilder withStartDate(String date) {
        this.startDate = new Date8(date);
        return this;
    }

    /**
     * Sets the end date of the contract.
     *
     * @param date the end date in ddMMyyyy format
     * @return this builder instance for chaining
     */
    public ContractBuilder withEndDate(String date) {
        this.endDate = new Date8(date);
        return this;
    }

    /**
     * Sets the amount of the contract.
     *
     * @param amount the amount in long format
     * @return this builder instance for chaining
     */
    public ContractBuilder withAmount(Long amount) {
        this.amount = new Amount(amount);
        return this;
    }

    /**
     * Builds and returns the {@link Contract} with the current attributes.
     *
     * @return a new {@code Contract} object
     */
    public Contract build() {
        return new Contract(athlete, sport, organization, startDate, endDate, amount);
    }
}
