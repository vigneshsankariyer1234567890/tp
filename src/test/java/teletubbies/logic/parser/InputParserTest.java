package teletubbies.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static teletubbies.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static teletubbies.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static teletubbies.logic.parser.CliSyntax.PREFIX_REMARK;
import static teletubbies.testutil.Assert.assertThrows;
import static teletubbies.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import teletubbies.commons.core.UserProfile;
import teletubbies.logic.commands.AddCommand;
import teletubbies.logic.commands.ClearCommand;
import teletubbies.logic.commands.ConfirmExportCommand;
import teletubbies.logic.commands.DeleteCommand;
import teletubbies.logic.commands.DoneCommand;
import teletubbies.logic.commands.EditCommand;
import teletubbies.logic.commands.EditCommand.EditPersonDescriptor;
import teletubbies.logic.commands.ExitCommand;
import teletubbies.logic.commands.ExportCommand;
import teletubbies.logic.commands.FindCommand;
import teletubbies.logic.commands.HelpCommand;
import teletubbies.logic.commands.HistoryCommand;
import teletubbies.logic.commands.ImportCommand;
import teletubbies.logic.commands.ListCommand;
import teletubbies.logic.commands.ProfileCommand;
import teletubbies.logic.commands.RemarkCommand;
import teletubbies.logic.parser.exceptions.ParseException;
import teletubbies.model.person.NameContainsKeywordsPredicate;
import teletubbies.model.person.Person;
import teletubbies.model.person.Phone;
import teletubbies.model.person.Remark;
import teletubbies.testutil.EditPersonDescriptorBuilder;
import teletubbies.testutil.PersonBuilder;
import teletubbies.testutil.PersonUtil;

public class InputParserTest {

    private final InputParser parser = new InputParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_deleteWithPhone() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + "p/87654321");
        assertEquals(new DeleteCommand(new Phone("87654321")), command);
    }

    @Test
    public void parseCommand_deleteWithIndex() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " i/" + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_remark() throws Exception {
        final Remark remark = new Remark("Some remark.");
        RemarkCommand command = (RemarkCommand) parser.parseCommand(RemarkCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_REMARK + remark.value);
        assertEquals(new RemarkCommand(INDEX_FIRST_PERSON, remark), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_import() throws Exception {
        assertTrue(parser.parseCommand(ImportCommand.COMMAND_WORD) instanceof ImportCommand);
        assertTrue(parser.parseCommand(ImportCommand.COMMAND_WORD + " 3") instanceof ImportCommand);
    }

    @Test
    public void parseCommand_export() throws Exception {
        assertTrue(parser.parseCommand(ExportCommand.COMMAND_WORD) instanceof ExportCommand);
        assertTrue(parser.parseCommand(ExportCommand.COMMAND_WORD + " \t hi") instanceof ExportCommand);
        assertTrue(parser.parseCommand(ExportCommand.COMMAND_WORD + " ignore") instanceof ExportCommand);

    }

    @Test
    public void parseCommand_confirmExport() throws Exception {
        assertTrue(parser.parseCommand(ConfirmExportCommand.COMMAND_WORD) instanceof ConfirmExportCommand);
        assertTrue(parser.parseCommand(ConfirmExportCommand.COMMAND_WORD + " \t hi") instanceof ConfirmExportCommand);
        assertTrue(parser.parseCommand(ConfirmExportCommand.COMMAND_WORD + " ignore") instanceof ConfirmExportCommand);

    }

    @Test
    public void parseCommand_done() throws Exception {
        DoneCommand command = (DoneCommand) parser.parseCommand(DoneCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DoneCommand(INDEX_FIRST_PERSON), command);

    }

    @Test
    public void parseCommand_profile() throws Exception {
        ProfileCommand command = (ProfileCommand) parser.parseCommand(
                ProfileCommand.COMMAND_WORD + " n/Name r/Telemarketer");
        assertEquals(new ProfileCommand(new UserProfile("Name", UserProfile.Role.TELEMARKETER)), command);
    }

    @Test
    public void parseCommand_history() throws Exception {
        HistoryCommand command = (HistoryCommand) parser.parseCommand(HistoryCommand.COMMAND_WORD);
        assertEquals(new HistoryCommand(), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
