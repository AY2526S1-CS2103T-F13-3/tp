package seedu.address.model.organization;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

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
