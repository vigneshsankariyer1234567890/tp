package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;

import java.util.stream.Stream;

import seedu.address.commons.core.UserProfile;
import seedu.address.logic.commands.ProfileCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ProfileCommand object
 */
public class ProfileCommandParser implements Parser<ProfileCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code ProfileCommand}
     * and returns a {@code ProfileCommand} object for execution.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public ProfileCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ROLE);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ROLE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ProfileCommand.MESSAGE_USAGE));
        }

        try {
            UserProfile userProfile = getUserProfileFromArgs(argMultimap);
            return new ProfileCommand(userProfile);
        } catch (ParseException e) {
            throw e;
        }
    }

    /**
     * Returns a UserProfile object constructed using the name and the role parsed from the given
     * {@code ArgumentMultimap}.
     *
     * @param argMultimap The Arguments passed into the parser as {@code ArgumentMultimap}.
     * @return A UserProfile object corresponding to the name and the role passed into the parser.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    private UserProfile getUserProfileFromArgs(ArgumentMultimap argMultimap) throws ParseException {
        try {
            String name = argMultimap.getValue(PREFIX_NAME).get();
            String roleString = argMultimap.getValue(PREFIX_ROLE).get();
            String upperCaseRoleString = roleString.toUpperCase();
            UserProfile.Role role = UserProfile.Role.valueOf(upperCaseRoleString);
            return new UserProfile(name, role);
        } catch (IllegalArgumentException e) {
            throw new ParseException(ProfileCommand.MESSAGE_INVALID_ROLE);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
