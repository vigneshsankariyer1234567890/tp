package teletubbies.logic.commands.uieffects;

import static java.util.Objects.requireNonNull;

import teletubbies.commons.core.LogsCenter;
import teletubbies.commons.exceptions.DataConversionException;
import teletubbies.commons.util.StringUtil;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.AddressBook;
import teletubbies.model.Model;
import teletubbies.storage.JsonAddressBookStorage;
import teletubbies.ui.MainWindow;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class ExportUiConsumer implements UiConsumer {

    private Model model;
    private final Logger logger = LogsCenter.getLogger(getClass());


    public ExportUiConsumer(Model model) {
        this.model = model;
    }

    @Override
    public void accept(MainWindow window) throws CommandException, DataConversionException {
        File fileToSave = window.handleFileChooser("Export Contacts File",
                MainWindow.FileSelectType.SAVE);
        requireNonNull(fileToSave);

        String pathString = includeDotJson(fileToSave.getPath());
        AddressBook ab = model.getExportAddressBook();
        saveAddressBookToPath(ab, pathString);
    }


    /**
     * Saves given address book to given path
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
}