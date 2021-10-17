package teletubbies.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static teletubbies.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import teletubbies.commons.core.UserProfile;
import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.UserPrefs;
import teletubbies.testutil.TypicalPersons;

class ProfileCommandTest {

    private Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validTelemarketerRole_success() {
        UserProfile userProfile = new UserProfile("Telemarketer Name", UserProfile.Role.TELEMARKETER);
        ProfileCommand profileCommand = new ProfileCommand(userProfile);

        String expectedMessage = String.format(ProfileCommand.MESSAGE_PROFILE_SUCCESS, userProfile);

        Model expectedModel = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setUserProfile(userProfile);
        expectedModel.setUserPrefs(userPrefs);

        CommandTestUtil.assertCommandSuccess(profileCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validSupervisorRole_success() {
        UserProfile userProfile = new UserProfile("Supervisor Name", UserProfile.Role.SUPERVISOR);
        ProfileCommand profileCommand = new ProfileCommand(userProfile);

        String expectedMessage = String.format(ProfileCommand.MESSAGE_PROFILE_SUCCESS, userProfile);

        Model expectedModel = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setUserProfile(userProfile);
        expectedModel.setUserPrefs(userPrefs);

        CommandTestUtil.assertCommandSuccess(profileCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        final UserProfile userProfile = new UserProfile("Name", UserProfile.Role.TELEMARKETER);
        final ProfileCommand standardCommand = new ProfileCommand(userProfile);

        // same values -> returns true
        ProfileCommand commandWithSameValues = new ProfileCommand(userProfile);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different name -> returns false
        assertFalse(standardCommand.equals(
                new ProfileCommand(new UserProfile("DiffName", UserProfile.Role.TELEMARKETER))));

        // different role -> returns false
        assertFalse(standardCommand.equals(
                new ProfileCommand(new UserProfile("Name", UserProfile.Role.SUPERVISOR))));
    }
}
