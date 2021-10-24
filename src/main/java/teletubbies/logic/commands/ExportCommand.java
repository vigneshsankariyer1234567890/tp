package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.util.Pair;
import teletubbies.commons.core.LogsCenter;
import teletubbies.commons.util.StringUtil;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.AddressBook;
import teletubbies.model.Model;
import teletubbies.model.person.Person;
import teletubbies.model.tag.TagUtils;
import teletubbies.storage.JsonAddressBookStorage;
import teletubbies.ui.MainWindow;

public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";
    public static final String MESSAGE_SUCCESS = "Contacts exported successfully.";

    private final Set<Pair<String, Optional<String>>> tags;
    private final Logger logger = LogsCenter.getLogger(getClass()); // TODO make a singleton logger or something

    public ExportCommand(Set<Pair<String, Optional<String>>> tags) {
        this.tags = tags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        String feedbackMessage = MESSAGE_SUCCESS + "\n"
                + (tags.isEmpty() ? "All contacts exported" : "Exported tags: " + tags);

        return new CommandResult(feedbackMessage, CommandResult.UiEffect.EXPORT, mainWindow -> {
            File fileToSave = mainWindow.handleFileChooser("Export Contacts File",
                    MainWindow.FileSelectType.SAVE);
            requireNonNull(fileToSave);

            String pathString = includeDotJson(fileToSave.getPath());
            AddressBook ab = filteredAddressBook(model);
            saveAddressBookToPath(ab, pathString);
        });
    }

    /**
     * Saves given addressbook to given path
     *
     * @param ab AddressBook to save
     * @param pathString Path string for saving
     * @throws CommandException
     */
    public void saveAddressBookToPath(AddressBook ab, String pathString) throws CommandException {
        try {
            new JsonAddressBookStorage(Paths.get(pathString)).saveAddressBook(ab);
        } catch (IOException ioe) {
            logger.warning("Failed to save contacts file : " + StringUtil.getDetails(ioe));
            throw new CommandException(ioe.getMessage());
        }
    }

    /**
     * Filters persons to those with specified tags
     *
     * @param model Model
     * @return filtered address book
     */
    public AddressBook filteredAddressBook(Model model) {
        requireNonNull(tags);

        ObservableList<Person> personObservableList = model.getFilteredPersonList();
        List<Person> personList = personObservableList.stream()
                .filter(TagUtils.personHasTagPredicate(tags)).collect(Collectors.toList());
        AddressBook ab = new AddressBook();
        ab.setPersons(personList);
        return ab;
    }

    /**
     * Add a '.json' to the end of filename if
     * not included already
     *
     * @param path path with or without .json
     * @return path with .json
     */
    public String includeDotJson(String path) {
        if (!path.endsWith(".json")) {
            path += ".json";
        }
        return path;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExportCommand)) {
            return false;
        }

        // state check
        ExportCommand c = (ExportCommand) other;
        return c.tags.containsAll(tags) && tags.containsAll(c.tags);
    }
}
