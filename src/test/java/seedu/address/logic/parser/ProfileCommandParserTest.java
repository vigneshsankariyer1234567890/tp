package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.UserProfile;
import seedu.address.logic.commands.ProfileCommand;

class ProfileCommandParserTest {
    private ProfileCommandParser parser = new ProfileCommandParser();

    @Test
    public void parse_validTelemarketerRole_success() {
        String userInput = " n/Telemarketer Name r/Telemarketer";
        UserProfile userProfile = new UserProfile("Telemarketer Name", UserProfile.Role.TELEMARKETER);
        ProfileCommand expectedCommand = new ProfileCommand(userProfile);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validSupervisorRole_success() {
        String userInput = " n/Supervisor Name r/Supervisor";
        UserProfile userProfile = new UserProfile("Supervisor Name", UserProfile.Role.SUPERVISOR);
        ProfileCommand expectedCommand = new ProfileCommand(userProfile);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidRole_failure() {
        String userInput = " n/Name r/Invalid";
        assertParseFailure(parser, userInput, ProfileCommand.MESSAGE_INVALID_ROLE);
    }

    @Test
    public void parse_noName_failure() {
        String userInput = " n/Name";
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ProfileCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noRole_failure() {
        String userInput = " r/Invalid";
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ProfileCommand.MESSAGE_USAGE));
    }
}
