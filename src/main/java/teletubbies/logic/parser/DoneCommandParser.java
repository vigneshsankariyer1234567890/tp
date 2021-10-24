package teletubbies.logic.parser;

import static java.util.Objects.requireNonNull;
import static teletubbies.logic.parser.CliSyntax.PREFIX_INCOMPLETE;
import static teletubbies.logic.parser.CliSyntax.PREFIX_ONGOING;

import teletubbies.commons.core.Messages;
import teletubbies.commons.core.index.Index;
import teletubbies.logic.commands.DoneCommand;
import teletubbies.logic.parser.exceptions.ParseException;
import teletubbies.model.tag.CompletionStatusTag.CompletionStatus;

/**
 * Parses input arguments and creates a new {@code DoneCommand} object
 */
public class DoneCommandParser implements Parser<DoneCommand> {

    private static final String INVALID_FLAG = "You can only use one completion status flag";

    /**
     * Parses the given {@code String} of arguments in the context of the {@code DoneCommand}
     * and returns a {@code DoneCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public DoneCommand parse(String args) throws ParseException {
        requireNonNull(args);

        try {
            ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ONGOING, PREFIX_INCOMPLETE);
            Index index = ParserUtil.parseIndex(argumentMultimap.getPreamble());

            boolean incomplete = argumentMultimap.getAllValues(PREFIX_INCOMPLETE).isPresent();
            boolean ongoing  = argumentMultimap.getAllValues(PREFIX_ONGOING).isPresent();
            if (incomplete && ongoing) throw new ParseException(INVALID_FLAG);
            CompletionStatus completionStatus = incomplete
                    ? CompletionStatus.INCOMPLETE
                    : ongoing
                        ? CompletionStatus.ONGOING
                        : CompletionStatus.COMPLETE;
            return new DoneCommand(index, completionStatus);
        } catch (ParseException parseException) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE), parseException
            );
        }

    }
}
