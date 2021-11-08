package teletubbies.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("-n");
    public static final Prefix PREFIX_PHONE = new Prefix("-p");
    public static final Prefix PREFIX_EMAIL = new Prefix("-e");
    public static final Prefix PREFIX_ADDRESS = new Prefix("-a");
    public static final Prefix PREFIX_TAG = new Prefix("-t");
    public static final Prefix PREFIX_ROLE = new Prefix("-role");
    public static final Prefix PREFIX_INDEX = new Prefix("-i");
    public static final Prefix PREFIX_REMARK = new Prefix("-r");
    public static final Prefix PREFIX_ONGOING = new Prefix("-ong");
    public static final Prefix PREFIX_INCOMPLETE = new Prefix("-inc");
    public static final Prefix PREFIX_VALUE = new Prefix("-v");
    public static final Prefix PREFIX_SUPERVISOR_FLAG = new Prefix("-s");
    public static final Prefix PREFIX_UUID = new Prefix("-u"); //Only used for testing AddCommandParser

    public static final String DEFAULT_EMAIL = "hello@tp.com";
    public static final String DEFAULT_ADDRESS = "NUS";
    public static final String EMPTY_REMARK = "";

}
