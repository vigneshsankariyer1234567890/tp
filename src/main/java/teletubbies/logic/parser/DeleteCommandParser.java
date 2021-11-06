package teletubbies.logic.parser;

import static teletubbies.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static teletubbies.logic.parser.CliSyntax.PREFIX_INDEX;
import static teletubbies.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.stream.Stream;

import teletubbies.commons.core.Messages;
import teletubbies.commons.core.index.Index;
import teletubbies.logic.commands.DeleteCommand;
import teletubbies.logic.parser.exceptions.ParseException;
import teletubbies.model.person.Phone;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    public static final String MESSAGE_INVALID_REFERENCE_METHOD = "A person can be referenced by either phone "
            + "number or index, but not both!";


    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {


        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PHONE, PREFIX_INDEX);

        if ((!arePrefixesPresent(argMultimap, PREFIX_PHONE) && !arePrefixesPresent(argMultimap, PREFIX_INDEX))
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        if (arePrefixesPresent(argMultimap, PREFIX_PHONE) && arePrefixesPresent(argMultimap, PREFIX_INDEX)) {
            throw new ParseException(MESSAGE_INVALID_REFERENCE_METHOD);
        }

        try {
            if (arePrefixesPresent(argMultimap, PREFIX_PHONE)) {
                Phone phoneNumber = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
                return new DeleteCommand(phoneNumber);
            } else {
                Index index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_INDEX).get());
                return new DeleteCommand(index);
            }
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
        }
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
