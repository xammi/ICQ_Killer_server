package confparser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 22.03.15.
 */
public class Section {
    private String name;
    private final Map<String, String> optionValues = new HashMap<>();
    private final Map<String, String> optionTypes = new HashMap<>();

    public Section() {}

    public Section(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getOption(String optionName, String typeName) throws WrongOptionType {
        String rightType = getOptionType(optionName);
        if (typeName.equals(rightType)) {
            return this.optionValues.get(optionName);
        }
        else {
            throw new WrongOptionType(optionName, typeName, rightType);
        }
    }

    public String getOptionType(String optionName) {
        return this.optionTypes.get(optionName);
    }

    public void addOption(String optionName, String value, String type) {
        this.optionValues.put(optionName, value);
        this.optionTypes.put(optionName, type);
    }
}
