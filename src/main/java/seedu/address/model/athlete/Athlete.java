package seedu.address.model.athlete;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.contract.Contract;
import seedu.address.model.organization.Organization;

/**
 * Represents an Athlete in playbook.io.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Athlete {

    // Identity fields
    private final Name name;
    private final Sport sport;
    private final Phone phone;
    private final Email email;
    private final Age age;


    /**
     * Every field must be present and not null.
     */
    public Athlete(Name name, Sport sport, Age age, Phone phone, Email email) {
        requireAllNonNull(name, sport, age, phone, email);
        this.name = name;
        this.sport = sport;
        this.age = age;
        this.phone = phone;
        this.email = email;
    }

    public Name getName() {
        return name;
    }

    public Sport getSport() {
        return sport;
    }

    public Age getAge() {
        return age;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public ObservableList<Organization> getOrganizations(ObservableList<Contract> allContracts) {
        if (allContracts == null || allContracts.isEmpty()) {
            return FXCollections.observableArrayList();
        }

        return allContracts.stream()
                .filter(contract -> contract.getAthlete().isSameAthlete(this))
                .map(contract -> contract.getOrganization())
                .distinct()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public int getTotalContractAmount(ObservableList<Contract> allContracts) {
        if (allContracts == null || allContracts.isEmpty()) {
            return 0;
        }

        return allContracts.stream()
                .filter(contract -> contract.getAthlete().isSameAthlete(this))
                .mapToInt(contract -> contract.getAmount().value)
                .sum();
    }

    /**
     * Returns true if both athletes have the same name and sport.
     * This defines a weaker notion of equality between two athletes.
     */
    public boolean isSameAthlete(Athlete otherAthlete) {
        if (otherAthlete == this) {
            return true;
        }

        return otherAthlete != null
                && otherAthlete.getName().equals(getName()) && otherAthlete.getSport().equals(getSport());
    }

    /**
     * Returns true if both athletes have the same identity fields.
     * This defines a stronger notion of equality between two athletes.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Athlete)) {
            return false;
        }

        Athlete otherAthlete = (Athlete) other;
        return name.equals(otherAthlete.name)
                && sport.equals(otherAthlete.sport)
                && age.equals(otherAthlete.age)
                && phone.equals(otherAthlete.phone)
                && email.equals(otherAthlete.email);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, sport, age, phone, email);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("sport", sport)
                .add("age", age)
                .add("phone", phone)
                .add("email", email)
                .toString();
    }

}
