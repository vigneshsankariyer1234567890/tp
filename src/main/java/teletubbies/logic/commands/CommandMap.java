package teletubbies.logic.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandMap {

    private static final Map<String, Class<? extends Command>> classMap = new HashMap<>();

    static {
        classMap.put(AddCommand.COMMAND_WORD, AddCommand.class);
        classMap.put(ClearCommand.COMMAND_WORD, ClearCommand.class);
        classMap.put(ConfirmExportCommand.COMMAND_WORD, ConfirmExportCommand.class);
        classMap.put(DeleteCommand.COMMAND_WORD, DeleteCommand.class);
        classMap.put(DoneCommand.COMMAND_WORD, DoneCommand.class);
        classMap.put(EditCommand.COMMAND_WORD, EditCommand.class);
        classMap.put(ExitCommand.COMMAND_WORD, ExitCommand.class);
        classMap.put(ExportCommand.COMMAND_WORD, ExportCommand.class);
        classMap.put(FilterCommand.COMMAND_WORD, FilterCommand.class);
        classMap.put(FindCommand.COMMAND_WORD, FindCommand.class);
        classMap.put(HelpCommand.COMMAND_WORD, HelpCommand.class);
        classMap.put(HistoryCommand.COMMAND_WORD, HistoryCommand.class);
        classMap.put(ImportCommand.COMMAND_WORD, ImportCommand.class);
        classMap.put(ListCommand.COMMAND_WORD, ListCommand.class);
        classMap.put(ProfileCommand.COMMAND_WORD, ProfileCommand.class);
        classMap.put(RemoveTagCommand.COMMAND_WORD, RemoveTagCommand.class);
        classMap.put(TagCommand.COMMAND_WORD, TagCommand.class);
        classMap.put(RemarkCommand.COMMAND_WORD, RemarkCommand.class);
    }

    public static Class<? extends Command> getClass(String commandWord) {
        return classMap.get(commandWord);
    }

}
