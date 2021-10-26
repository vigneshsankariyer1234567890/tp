package teletubbies.logic.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import teletubbies.commons.core.Messages;
import teletubbies.logic.commands.AddCommand;
import teletubbies.logic.commands.ClearCommand;
import teletubbies.logic.commands.Command;
import teletubbies.logic.commands.ConfirmExportCommand;
import teletubbies.logic.commands.DeleteCommand;
import teletubbies.logic.commands.DoneCommand;
import teletubbies.logic.commands.EditCommand;
import teletubbies.logic.commands.ExitCommand;
import teletubbies.logic.commands.ExportCommand;
import teletubbies.logic.commands.FilterCommand;
import teletubbies.logic.commands.FindCommand;
import teletubbies.logic.commands.HelpCommand;
import teletubbies.logic.commands.HistoryCommand;
import teletubbies.logic.commands.ImportCommand;
import teletubbies.logic.commands.ListCommand;
import teletubbies.logic.commands.ProfileCommand;
import teletubbies.logic.commands.RemarkCommand;
import teletubbies.logic.commands.RemoveTagCommand;
import teletubbies.logic.commands.TagCommand;
import teletubbies.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class InputParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case DoneCommand.COMMAND_WORD:
            return new DoneCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case ProfileCommand.COMMAND_WORD:
            return new ProfileCommandParser().parse(arguments);

        case ImportCommand.COMMAND_WORD:
            return new ImportCommand();

        case ExportCommand.COMMAND_WORD:
            return new ExportCommandParser().parse(arguments);

        case TagCommand.COMMAND_WORD:
            return new TagCommandParser().parse(arguments);

        case RemoveTagCommand.COMMAND_WORD:
            return new RemoveTagCommandParser().parse(arguments);

        case FilterCommand.COMMAND_WORD:
            return new FilterCommandParser().parse(arguments);

        case ConfirmExportCommand.COMMAND_WORD:
            return new ConfirmExportCommand();

        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommand();

        case RemarkCommand.COMMAND_WORD:
            return new RemarkCommandParser().parse(arguments);

        default:
            throw new ParseException(Messages.MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
