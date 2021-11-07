package teletubbies.logic.parser;

import static java.util.Objects.requireNonNull;
import static teletubbies.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static teletubbies.logic.parser.CliSyntax.PREFIX_REMARK;

import teletubbies.commons.core.index.Index;
import teletubbies.commons.exceptions.IllegalValueException;
import teletubbies.logic.commands.RemarkCommand;
import teletubbies.logic.parser.exceptions.ParseException;
import teletubbies.model.person.Remark;

/**
 * Parses input arguments and creates a new {@code RemarkCommand} object
 */
public class RemarkCommandParser implements Parser<RemarkCommand> {

    public static final String MESSAGE_EXCEED_REMARK_CHARACTER_LIMIT = "There is a 40 character limit for a remark!";

    // @@author: j-lum
    // Reused from
    // https://github.com/se-edu/addressbook-level3/compare/tutorial-add-remark
    /**
     * Parses the given {@code String} of arguments in the context of the {@code RemarkCommand}
     * and returns a {@code RemarkCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_REMARK);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE), ive);
        }

        String remark = argMultimap.getValue(PREFIX_REMARK).orElse("");

        int numberOfCharacters = remark.length();

        if (numberOfCharacters > 40) {
            throw new ParseException(MESSAGE_EXCEED_REMARK_CHARACTER_LIMIT);
        }

        return new RemarkCommand(index, new Remark(remark));
    }
}
