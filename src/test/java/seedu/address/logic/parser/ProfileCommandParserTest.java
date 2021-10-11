package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.UserProfile;
import seedu.address.logic.commands.ProfileCommand;

class ProfileCommandParserTest {
    private ProfileCommandParser parser = new ProfileCommandParser();

    @Test
    public void parse_validTelemarketerRole_Success() {
        String userInput = " n/Telemarketer Name r/Telemarketer";
        UserProfile userProfile = new UserProfile("Telemarketer Name", UserProfile.Role.TELEMARKETER);
        ProfileCommand expectedCommand = new ProfileCommand(userProfile);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validSupervisorRole_Success() {
        String userInput = " n/Supervisor Name r/Supervisor";
        UserProfile userProfile = new UserProfile("Supervisor Name", UserProfile.Role.SUPERVISOR);
        ProfileCommand expectedCommand = new ProfileCommand(userProfile);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidRole_Failure() {
        String userInput = " n/Name r/Invalid";
        assertParseFailure(parser, userInput, ProfileCommand.MESSAGE_INVALID_ROLE);
    }
}
