package teletubbies.commons.core;

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
        this.name = "";
        this.role = Role.TELEMARKETER;
    }

    /**
     * Constructs a {@code UserProfile} with the specified name and role.
     */
    public UserProfile(String name, Role userRole) {
        this.name = name;
        this.role = userRole;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public String getRoleString() {
        return role.toString();
    }

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
        sb.append("Name : ").append(name).append("\n");
        sb.append("Role : ").append(role).append("\n");
        return sb.toString();
    }
}
