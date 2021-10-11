package seedu.address.commons.core;

import java.io.Serializable;
import java.util.Objects;

public class UserProfile {

    private String name;
    private Role role;
    public enum Role implements Serializable {
        TELEMARKETER,
        SUPERVISOR
    }

    /**
     * Constructs a {@code UserProfile} with no name.
     */
    public UserProfile() {
        this.name = null;
        this.role = null;
    }

    /**
     * Constructs a {@code UserProfile} with the specified name.
     */
    public UserProfile(String name) {
        this.name = name;
        this.role = null;
    }

    /**
     * Constructs a {@code UserProfile} with the specified name and role.
     */
//    public UserProfile(String name, Role userRole) {
//        this.name = name;
//        this.role = userRole;
//    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserProfile)) { //this handles null as well.
            return false;
        }

        UserProfile o = (UserProfile) other;

        return name.equals(o.name)
                && role.equals(o.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name : ").append(name).append("\n");
        sb.append("role : ").append(role).append("\n");
        return sb.toString();
    }
}
