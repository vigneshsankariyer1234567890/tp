package teletubbies.logic.commands;

import static java.util.Objects.requireNonNull;

import teletubbies.model.Model;
import teletubbies.ui.MainWindow;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting Teletubbies as requested ...";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.cancelPendingExport();
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, CommandResult.UiEffect.EXIT, MainWindow::handleExit);
    }

}
