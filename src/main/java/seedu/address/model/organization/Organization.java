package seedu.address.model.organization;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.athlete.Athlete;
import seedu.address.model.contract.Contract;

/**
 * Represents an Organization in playbook.io.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Organization {

    // Identity fields
    private final OrganizationName name;
    private final OrganizationPhone phone;
    private final OrganizationEmail email;

    /**
     * Every field must be present and not null.
     */
    public Organization(OrganizationName name, OrganizationPhone phone, OrganizationEmail email) {
        requireAllNonNull(name, phone, email);
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public OrganizationName getName() {
        return name;
    }

    public OrganizationPhone getPhone() {
        return phone;
    }

    public OrganizationEmail getEmail() {
        return email;
    }

    private static Predicate<Athlete> distinctBySameAthlete() {
        final List<Athlete> seen = new ArrayList<>();
        return c -> {
            boolean dup = seen.stream().anyMatch(existing -> existing.isSameAthlete(c));
            if (!dup) {
                seen.add(c);
                return true;
            }
            return false;
        };
    }

    /**
     * Returns a list of all athletes associated with this organization, given a list of all contracts.
     *
     * @param allContracts The list of all contracts.
     * @return An unmodifiable observable list of athletes.
     */
    public ObservableList<Athlete> getAthletes(ObservableList<Contract> allContracts) {
        if (allContracts == null || allContracts.isEmpty()) {
            return FXCollections.observableArrayList();
        }

        return allContracts.stream()
                .filter(contract -> isSameOrganization(contract.getOrganization()))
                .map(Contract::getAthlete)
                .filter(distinctBySameAthlete())
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * Returns the total contract amount associated with this organization.
     *
     * @param allContracts The list of all contracts.
     * @return The total sum of contract amounts for this organization.
     */
    public long getTotalContractAmount(ObservableList<Contract> allContracts) {
        if (allContracts == null || allContracts.isEmpty()) {
            return 0;
        }

        return allContracts.stream()
                .filter(contract -> isSameOrganization(contract.getOrganization()))
                .mapToLong(contract -> contract.getAmount().value)
                .sum();
    }

    /**
     * Returns true if both Organizations have the same name.
     * This defines a weaker notion of equality between two Organizations.
     */
    public boolean isSameOrganization(Organization otherOrganization) {
        if (otherOrganization == this) {
            return true;
        }
        return otherOrganization != null
                && otherOrganization.getName().equals(name);
    }

    /**
     * Returns true if both Organizations have the same identity and data fields.
     * This defines a stronger notion of equality between two Organizations.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Organization)) {
            return false;
        }

        Organization otherOrg = (Organization) other;
        return name.equals(otherOrg.name)
                && phone.equals(otherOrg.phone)
                && email.equals(otherOrg.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .toString();
    }
}
