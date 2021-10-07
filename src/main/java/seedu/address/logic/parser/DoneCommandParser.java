package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DoneCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code DoneCommand} object
 */
public class DoneCommandParser implements Parser<DoneCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code DoneCommand}
     * and returns a {@code DoneCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public DoneCommand parse(String args) throws ParseException {
        requireNonNull(args);

        try {
            Index index = ParserUtil.parseIndex(args);
            return new DoneCommand(index);
        } catch (ParseException parseException) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE), parseException
            );
        }

    }
}
