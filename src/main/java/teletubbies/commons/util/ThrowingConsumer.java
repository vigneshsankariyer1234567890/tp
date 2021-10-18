package teletubbies.commons.util;

import teletubbies.commons.exceptions.DataConversionException;
import teletubbies.logic.commands.exceptions.CommandException;

@FunctionalInterface
public interface ThrowingConsumer<T> {

    void accept(T input) throws CommandException, DataConversionException;

}
