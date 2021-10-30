package teletubbies.logic.parser;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import teletubbies.commons.core.Messages;
import teletubbies.logic.commands.AddCommand;
import teletubbies.logic.parser.exceptions.ParseException;
import teletubbies.model.person.Address;
import teletubbies.model.person.Email;
import teletubbies.model.person.Name;
import teletubbies.model.person.Person;
import teletubbies.model.person.Phone;
import teletubbies.model.person.Remark;
import teletubbies.model.person.Uuid;
import teletubbies.model.tag.CompletionStatusTag;
import teletubbies.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_NAME, CliSyntax.PREFIX_PHONE, CliSyntax.PREFIX_EMAIL,
                        CliSyntax.PREFIX_ADDRESS);

        if (!arePrefixesPresent(argMultimap, CliSyntax.PREFIX_NAME, CliSyntax.PREFIX_PHONE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(CliSyntax.PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(CliSyntax.PREFIX_PHONE).get());

        Email email = ParserUtil.parseEmail(CliSyntax.DEFAULT_EMAIL);
        if (arePrefixesPresent(argMultimap, CliSyntax.PREFIX_EMAIL)) {
            email = ParserUtil.parseEmail(argMultimap.getValue(CliSyntax.PREFIX_EMAIL).get());
        }

        Address address = ParserUtil.parseAddress(CliSyntax.DEFAULT_ADDRESS);
        if (arePrefixesPresent(argMultimap, CliSyntax.PREFIX_ADDRESS)) {
            address = ParserUtil.parseAddress(argMultimap.getValue(CliSyntax.PREFIX_ADDRESS).get());
        }

        Remark remark = new Remark("");

        Set<Tag> tagList = new HashSet<>();

        String uuid = UUID.randomUUID().toString();
        Person person = new Person(new Uuid(uuid), name, phone, email, address,
                new CompletionStatusTag(), remark, tagList);

        return new AddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
