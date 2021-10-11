package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.JsonSerializableAddressBook;

public class ImportCommand extends Command {
    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "Click File -> Import in the menu bar and select file "
            + "to import.";
    public static final String MESSAGE_SUCCESS = "Contacts imported successfully.";
    public static final String MESSAGE_FILE_NOT_FOUND = "Data file not found. Will be starting with sample contacts";
    public static final String MESSAGE_INCORRECT_FORMAT = "Data file not in the correct format. Will be starting with "
            + "an empty AddressBook.";

    private final Path filePath;

    /**
     * Creates an ImportCommand to import a chosen file.
     * @param pathStr Path of the file to import.
     */
    public ImportCommand(String pathStr) {
        requireNonNull(pathStr);
        filePath = Path.of(pathStr);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Optional<ReadOnlyAddressBook> addressBookOptional;
        ReadOnlyAddressBook newContacts;
        try {
            addressBookOptional = readAddressBook(filePath);
            newContacts = addressBookOptional.orElseGet(SampleDataUtil::getSampleAddressBook);
            model.setAddressBook(newContacts);
            return new CommandResult(addressBookOptional.isPresent() ? MESSAGE_SUCCESS : MESSAGE_FILE_NOT_FOUND);
        } catch (DataConversionException e) {
            newContacts = new AddressBook();
            model.setAddressBook(newContacts);
            return new CommandResult(MESSAGE_INCORRECT_FORMAT);
        }
    }

    /**
     * Reads JSON file containing contacts and converts it to a {@link ReadOnlyAddressBook}.
     * @param filePath specifying file containing contacts to be read.
     * @return AddressBook data as a {@link ReadOnlyAddressBook} or {@code Optional.empty()} if file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     */
    public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableAddressBook> jsonAddressBook = JsonUtil.readJsonFile(
                filePath, JsonSerializableAddressBook.class);
        if (!jsonAddressBook.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonAddressBook.get().toModelType());
        } catch (IllegalValueException ive) {
            throw new DataConversionException(ive);
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ImportCommand)) {
            return false;
        }

        // state check
        ImportCommand e = (ImportCommand) other;
        return filePath.equals(e.filePath);
    }
}
