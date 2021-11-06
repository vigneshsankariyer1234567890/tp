package teletubbies.logic.commands.uiEffects;

import teletubbies.commons.exceptions.DataConversionException;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.ui.MainWindow;

public class HelpUiConsumer implements UiConsumer {

    @Override
    public void accept(MainWindow window) throws CommandException, DataConversionException {
        window.handleHelp();
    }
}
