package teletubbies.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javafx.util.Pair;
import teletubbies.logic.commands.ExportCommand;
import teletubbies.logic.parser.exceptions.ParseException;

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

        FlagValue tagString = argMultimap.getAllValues(CliSyntax.PREFIX_TAG);
        Set<Pair<String, Optional<String>>> tagSet = new HashSet<>();
        for (String v: tagString.getValues()) {
            String[] nameValuePair = v.trim().split(":");
            Optional<String> value = nameValuePair.length < 2
                    ? Optional.empty()
                    : Optional.of(nameValuePair[1].trim());
            tagSet.add(new Pair<>(nameValuePair[0].trim(), value));
        }

        return new ExportCommand(tagSet);
    }

}
