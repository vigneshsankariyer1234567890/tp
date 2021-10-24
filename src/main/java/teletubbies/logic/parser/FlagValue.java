package teletubbies.logic.parser;

import java.util.List;

public class FlagValue {

    public final Prefix prefix;
    private List<String> values;
    private boolean isPresent;

    /**
     * @param values list of values for the prefix flag
     * @param isPresent if prefix was used
     * @param prefix associated prefix
     */
    public FlagValue(List<String> values, boolean isPresent, Prefix prefix) {
        this.values = values;
        this.isPresent = isPresent;
        this.prefix = prefix;
    }

    /**
     * Add a value associated with the prefix
     *
     * @param value value to add
     * @return list of values after adding
     */
    public List<String> add(String value) {
        values.add(value);
        return values;
    }

    public void setPresent() {
        this.isPresent = true;
    }

    public List<String> getValues() {
        return this.values;
    }

    public boolean isPresent() {
        return this.isPresent;
    }

}
