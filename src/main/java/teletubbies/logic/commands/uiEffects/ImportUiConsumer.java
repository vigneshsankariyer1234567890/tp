package teletubbies.logic.commands.uiEffects;

import static java.util.Objects.requireNonNull;
import static teletubbies.logic.commands.ImportCommand.MESSAGE_FILE_NOT_FOUND;
import static teletubbies.logic.commands.ImportCommand.MESSAGE_INCORRECT_FORMAT;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import teletubbies.commons.core.LogsCenter;
import teletubbies.commons.exceptions.DataConversionException;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.Model;
import teletubbies.model.ReadOnlyAddressBook;
import teletubbies.storage.JsonAddressBookStorage;
import teletubbies.ui.MainWindow;

public class ImportUiConsumer implements UiConsumer {

    private Model model;
    private final Logger logger = LogsCenter.getLogger(getClass());


    public ImportUiConsumer(Model model) {
        this.model = model;
    }

    @Override
    public void accept(MainWindow window) throws CommandException, DataConversionException {
        try {
            File fileToImport = window.handleFileChooser("Import Contacts File",
                    MainWindow.FileSelectType.OPEN);
            requireNonNull(fileToImport);

            Path filePath = fileToImport.toPath();

            Optional<ReadOnlyAddressBook> addressBookOptional = new JsonAddressBookStorage(filePath)
                    .readAddressBook();
            if (addressBookOptional.isEmpty()) {
                throw new CommandException(MESSAGE_FILE_NOT_FOUND);
            }
            ReadOnlyAddressBook newContacts = addressBookOptional.get();
            model.setAddressBook(newContacts);
            model.commitAddressBook();
            logger.info("Imported contacts from " + filePath);
        } catch (DataConversionException e) {
            throw new CommandException(MESSAGE_INCORRECT_FORMAT);
        } catch (NullPointerException e) {
            throw new CommandException(MESSAGE_FILE_NOT_FOUND);
        }
    }
}
