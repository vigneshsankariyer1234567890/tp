package seedu.address.commons.util;

import seedu.address.logic.commands.exceptions.CommandException;

@FunctionalInterface
public interface ThrowingConsumer<T> {

    void accept(T input) throws CommandException;

}
