package teletubbies.commons.util;

@FunctionalInterface
public interface ThrowingConsumer<T> {

    void accept(T input) throws Exception;

}
