package teletubbies.logic.commands;

import java.util.List;

import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.Model;

/**
 * Command that returns the history in reverse order with the most recent at the top.
 */
public class HistoryCommand extends Command {
    public static final String COMMAND_WORD = "history";
    public static final String MESSAGE_SUCCESS = "Recent Commands:\n%1$s";
    public static final String MESSAGE_NO_HISTORY = "You have not entered any commands yet.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<String> history = model.getChronologicallyDescendingHistory();

        if (history.isEmpty()) {
            return new CommandResult(MESSAGE_NO_HISTORY);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, String.join("\n", history)));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        return obj instanceof HistoryCommand;
    }
}
