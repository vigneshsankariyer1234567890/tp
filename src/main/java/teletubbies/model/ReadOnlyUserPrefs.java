package teletubbies.model;

import java.nio.file.Path;

import teletubbies.commons.core.GuiSettings;
import teletubbies.commons.core.UserProfile;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    UserProfile getUserProfile();

    Path getAddressBookFilePath();

}
