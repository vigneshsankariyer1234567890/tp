package teletubbies.logic.commands.uieffects;

import static java.util.Objects.requireNonNull;
import static teletubbies.logic.commands.MergeCommand.MESSAGE_FILE_NOT_FOUND;
import static teletubbies.logic.commands.MergeCommand.MESSAGE_INCORRECT_FORMAT;

import javafx.collections.ObservableList;
import teletubbies.commons.core.LogsCenter;
import teletubbies.commons.exceptions.DataConversionException;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.Model;
import teletubbies.model.ReadOnlyAddressBook;
import teletubbies.model.person.Person;
import teletubbies.storage.JsonAddressBookStorage;
import teletubbies.ui.MainWindow;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;


public class MergeUiConsumer implements UiConsumer {
    private Model model;
    private final Logger logger = LogsCenter.getLogger(getClass());

    public MergeUiConsumer(Model model) {
        this.model = model;
    }

    @Override
    public void accept(MainWindow window) throws CommandException, DataConversionException {
        try {
            File fileToImport = window.handleFileChooser("Merge Contacts File",
                    MainWindow.FileSelectType.OPEN);
            requireNonNull(fileToImport);

            Path filePath = fileToImport.toPath();

            Optional<ReadOnlyAddressBook> addressBookOptional = new JsonAddressBookStorage(filePath)
                    .readAddressBook();
            if (addressBookOptional.isEmpty()) {
                throw new CommandException(MESSAGE_FILE_NOT_FOUND);
            }
            ReadOnlyAddressBook addressBookToMerge = addressBookOptional.get();
            ObservableList<Person> personsToMerge = addressBookToMerge.getPersonList();
            personsToMerge.stream().forEach(person -> model.mergePerson(person));
            model.commitAddressBook();

            logger.info("Merged contacts from " + filePath);
        } catch (DataConversionException e) {
            throw new CommandException(MESSAGE_INCORRECT_FORMAT);
        } catch (NullPointerException e) {
            throw new CommandException(MESSAGE_FILE_NOT_FOUND);
        }
    }
}
