package teletubbies.logic.parser;

import static java.util.Objects.requireNonNull;
import static teletubbies.logic.parser.CliSyntax.PREFIX_NAME;
import static teletubbies.logic.parser.CliSyntax.PREFIX_SUPERVISOR_FLAG;
import static teletubbies.logic.parser.CliSyntax.PREFIX_VALUE;

import java.util.Optional;

import teletubbies.commons.core.Messages;
import teletubbies.commons.core.Range;
import teletubbies.logic.commands.TagCommand;
import teletubbies.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code TagCommand} object
 */
public class TagCommandParser implements Parser<TagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code TagCommand}
     * and returns a {@code TagCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public TagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME,
                PREFIX_VALUE, PREFIX_SUPERVISOR_FLAG);
        String preambleString = argumentMultimap.getPreamble();
        if (preambleString.isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    TagCommand.MESSAGE_USAGE));
        }

        Range personRange = ParserUtil.parseRange(preambleString);

        Optional<String> tagName = argumentMultimap.getValue(PREFIX_NAME);
        if (tagName.isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    TagCommand.MESSAGE_USAGE));
        }
        String tagValue = argumentMultimap.getValue(PREFIX_VALUE).orElse("");

        boolean supervisorFlag = argumentMultimap.getAllValues(PREFIX_SUPERVISOR_FLAG).isPresent();

        return new TagCommand(personRange, tagName.get(), tagValue, supervisorFlag);
    }
}
