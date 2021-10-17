package teletubbies.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import teletubbies.logic.commands.ExportCommand;
import teletubbies.logic.parser.exceptions.ParseException;
import teletubbies.model.tag.Tag;

/**
 * Parses input arguments and creates a new ExportCommand object
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ExportCommand
     * and returns a ExportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ExportCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                CliSyntax.PREFIX_TAG);

        String tagString = argMultimap.getValue(CliSyntax.PREFIX_TAG).orElse("");
        List<String> tagList = Stream.of(tagString.split(" ")).map(String::trim)
                .filter(trim -> !trim.equals("")).collect(Collectors.toList());
        Set<Tag> tagSet = tagList.size() == 1 && tagList.get(0).equals("")
                ? new HashSet<>()
                : ParserUtil.parseTags(tagList);

        return new ExportCommand(tagSet);
    }

}
