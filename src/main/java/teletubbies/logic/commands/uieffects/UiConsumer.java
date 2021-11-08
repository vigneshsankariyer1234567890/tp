package teletubbies.logic.commands.uieffects;

import teletubbies.commons.exceptions.DataConversionException;
import teletubbies.commons.util.ThrowingConsumer;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.ui.MainWindow;

public interface UiConsumer extends ThrowingConsumer<MainWindow> {

    void accept(MainWindow input) throws CommandException, DataConversionException;

}
