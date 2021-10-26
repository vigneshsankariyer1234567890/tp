package teletubbies.ui;

import static java.util.Objects.requireNonNull;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Region;
import teletubbies.model.person.Person;
import teletubbies.model.tag.CompletionStatusTag;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ChartDisplay extends UiPart<Region> {

    private static final String FXML = "ChartDisplay.fxml";

    private ObservableList<Person> personList;

    @FXML
    private PieChart chartDisplay;

    /**
     * Creates a new ChartDisplay.
     *
     * @param personList The list of Persons to be checked.
     */
    public ChartDisplay(ObservableList<Person> personList) {
        super(FXML);
        requireNonNull(personList);
        this.personList = personList;
        loadChart();
    }

    /**
     * Loads the chart with the updated data.
     */
    public void loadChart() {

        int completePersonCount = 0;
        int ongoingPersonCount = 0;
        int incompletePersonCount = 0;

        for (Person person : personList) {

            // Gets the ENUM value
            // TODO: Improve code quality
            CompletionStatusTag completionStatusTag = person.getCompletionStatus();
            CompletionStatusTag.CompletionStatus status = completionStatusTag.status;

            // Increment relevant count accordingly
            if (status == CompletionStatusTag.CompletionStatus.COMPLETE) {
                completePersonCount++;
            } else if (status == CompletionStatusTag.CompletionStatus.INCOMPLETE) {
                incompletePersonCount++;
            } else {
                ongoingPersonCount++;
            }
        }

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Complete", completePersonCount),
                        new PieChart.Data("Ongoing", ongoingPersonCount),
                        new PieChart.Data("Incomplete", incompletePersonCount));

        chartDisplay.setTitle("Your Progress");
        chartDisplay.setData(pieChartData);
    }
}
