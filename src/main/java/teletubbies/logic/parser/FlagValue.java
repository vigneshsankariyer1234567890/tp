package teletubbies.logic.parser;

import java.util.List;

public class FlagValue {

    public List<String> values;
    public final Prefix prefix;
    private boolean isPresent;


    public FlagValue(List<String> values, boolean isPresent, Prefix prefix) {
        this.values = values;
        this.isPresent = isPresent;
        this.prefix = prefix;
    }

    public List<String> add(String value) {
        values.add(value);
        return values;
    }

    public void setPresent() {
        this.isPresent = true;
    }

    public boolean isPresent() {
        return this.isPresent;
    }

}
