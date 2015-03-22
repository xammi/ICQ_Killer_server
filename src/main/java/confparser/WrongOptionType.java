package confparser;

/**
 * Created by max on 22.03.15.
 */
public class WrongOptionType extends Exception {

    public WrongOptionType(String name, String requestType, String rightType) {
        super("Option " + name + " has type " + rightType + " instead of " + requestType);
    }
}
