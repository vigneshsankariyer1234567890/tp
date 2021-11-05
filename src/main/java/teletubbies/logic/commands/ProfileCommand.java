package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import teletubbies.commons.core.UserProfile;
import teletubbies.commons.exceptions.UserRoleSetException;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.logic.parser.CliSyntax;
import teletubbies.logic.parser.Prefix;
import teletubbies.model.Model;

/**
 * Sets the name and role of the Teletubbies user.
 */
public class ProfileCommand extends Command {

    public static final String COMMAND_WORD = "profile";

    public static final List<Prefix> REQUIRED_FLAGS = List.of(CliSyntax.PREFIX_NAME, CliSyntax.PREFIX_ROLE);

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets the name and role of the Teletubbies user.\n"
            + "Parameters: "
            + CliSyntax.PREFIX_NAME + " NAME "
            + CliSyntax.PREFIX_ROLE + " ROLE (must be either Telemarketer or Supervisor)\n"
            + "Example: " + COMMAND_WORD + " "
            + CliSyntax.PREFIX_NAME + " John Doe "
            + CliSyntax.PREFIX_ROLE + " Telemarketer";

    public static final String MESSAGE_PROFILE_SUCCESS = "User profile set.\n%1$s";
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
        model.cancelPendingExport();
        try {
            model.setUserProfile(userProfile);
        } catch (UserRoleSetException urse) {
            throw new CommandException(urse.getMessage());
        }

        return new CommandResult(String.format(MESSAGE_PROFILE_SUCCESS, userProfile));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ProfileCommand // instanceof handles nulls
                && userProfile.equals(((ProfileCommand) other).userProfile));
    }
}
