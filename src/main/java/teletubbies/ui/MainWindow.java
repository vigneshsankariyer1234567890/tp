package teletubbies.ui;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import teletubbies.commons.core.GuiSettings;
import teletubbies.commons.core.LogsCenter;
import teletubbies.commons.exceptions.EarliestVersionException;
import teletubbies.commons.exceptions.LatestVersionException;
import teletubbies.logic.Logic;
import teletubbies.logic.commands.Command;
import teletubbies.logic.commands.CommandMap;
import teletubbies.logic.commands.CommandResult;
import teletubbies.logic.commands.ExportCommand;
import teletubbies.logic.commands.ImportCommand;
import teletubbies.logic.commands.MergeCommand;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.logic.parser.Prefix;
import teletubbies.logic.parser.exceptions.ParseException;
import teletubbies.model.Model;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;
    private Model model;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private ResultDisplay resultDisplay;
    private ChartDisplay chartDisplay;
    private CommandBox commandBox;
    private HelpWindow helpWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane chartDisplayPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic, Model model) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.model = model;

        this.primaryStage.addEventHandler(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                case UP:
                    handleUpPress();
                    break;
                case DOWN:
                    handleDownPress();
                    break;
                case TAB:
                    handleTab();
                    break;
                default: /**/
                }
            }
        });

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    private void handleUpPress() {
        try {
            commandBox.setText(model.getPreviousCommand());
        } catch (EarliestVersionException eve) {
            logger.info("Info: No previous command");
        }
    }

    private void handleDownPress() {
        try {
            commandBox.setText(model.getNextCommand());
        } catch (LatestVersionException lve) {
            logger.info("Info: No next command");
        }
    }

    // REQUIRED_FIElDS must always be a List<Prefix>
    @SuppressWarnings("unchecked")
    private void handleTab() {
        String commandText = commandBox.getText().trim();
        Class<? extends Command> commandClass = CommandMap.getClass(commandText);
        if (commandClass == null) {
            logger.info("No such command");
            return;
        }
        try {
            List<String> requiredTags = ((List<Prefix>) commandClass.getDeclaredField("REQUIRED_FLAGS")
                    .get(null))
                    .stream().map(Prefix::toString).collect(Collectors.toList());
            commandBox.setText(commandText + " " + String.join(" ", requiredTags));
        } catch (Exception e) {
            logger.info("No compulsory fields: " + e.getMessage());
        }
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        personListPanel = new PersonListPanel(logic.getFilteredPersonList());
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        chartDisplay = new ChartDisplay(logic.getFilteredPersonList());
        chartDisplayPlaceholder.getChildren().add(chartDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    public void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * FileChooser selection type
     */
    public enum FileSelectType {
        OPEN, SAVE
    }

    /**
     * Opens a FileChooser window of specified
     * selection type
     *
     * @param windowTitle Title of FileChooser window
     * @param selectType Type of selection for FileChooser
     * @return chosen File
     */
    @FXML
    public File handleFileChooser(String windowTitle, FileSelectType selectType) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(windowTitle);
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        switch(selectType) {
        case OPEN:
            return fileChooser.showOpenDialog(primaryStage);
        case SAVE:
            return fileChooser.showSaveDialog(primaryStage);
        default:
            return null;
        }
    }

    @FXML
    private void handleImport() throws CommandException, ParseException {
        this.executeCommand(ImportCommand.COMMAND_WORD);
    }

    @FXML
    private void handleExport() throws CommandException, ParseException {
        this.executeCommand(ExportCommand.COMMAND_WORD);
    }

    @FXML
    private void handleMerge() throws CommandException, ParseException {
        this.executeCommand(MergeCommand.COMMAND_WORD);
    }

    public PersonListPanel getPersonListPanel() {
        return personListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see teletubbies.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());

            commandResult.executeUiEffect(this);
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            chartDisplay.loadChart();

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
