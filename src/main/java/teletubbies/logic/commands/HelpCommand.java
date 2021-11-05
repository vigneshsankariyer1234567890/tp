package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;

import teletubbies.model.Model;
import teletubbies.ui.MainWindow;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows user guide.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.cancelPendingExport();

        return new CommandResult(SHOWING_HELP_MESSAGE, CommandResult.UiEffect.SHOW_HELP, MainWindow::handleHelp);
    }
}
