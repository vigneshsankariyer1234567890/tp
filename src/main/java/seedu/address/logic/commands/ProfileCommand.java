package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;

import seedu.address.commons.core.UserProfile;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;

/**
 * Sets the name and role of the address book user.
 */
public class ProfileCommand extends Command {

    public static final String COMMAND_WORD = "profile";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets the name and role of the address book user. "
            + "Parameters: "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_ROLE + "ROLE (must be either Telemarketer or Supervisor)]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_ROLE + "Telemarketer";

    public static final String MESSAGE_PROFILE_SUCCESS = "User profile set: %1$s";
    public static final String MESSAGE_INVALID_ROLE = "Please indicate either \"Telemarketer\" or \"Supervisor\""
            + " as your role.";

    private final UserProfile userProfile;

    /**
     * Creates a ProfileCommand to add the specified {@code UserProfile}
     */
    public ProfileCommand(UserProfile userProfile) {
        requireNonNull(userProfile);
        this.userProfile = userProfile;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        UserPrefs editedUserPrefs = new UserPrefs();
        editedUserPrefs.setUserProfile(userProfile);
        model.setUserPrefs(editedUserPrefs);

        return new CommandResult(String.format(MESSAGE_PROFILE_SUCCESS, userProfile));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof seedu.address.logic.commands.ProfileCommand // instanceof handles nulls
                && userProfile.equals(((seedu.address.logic.commands.ProfileCommand) other).userProfile));
    }
}
