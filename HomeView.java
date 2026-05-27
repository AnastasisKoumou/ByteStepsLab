import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;

public class HomeView extends BorderPane {
    public HomeView(List<Lesson> lessons, int totalPoints, int highScore, Consumer<Lesson> onLessonSelected) {
        getStyleClass().add("app-root");
        setPadding(new Insets(34));

        VBox page = new VBox(28);
        page.setAlignment(Pos.TOP_CENTER);

        HBox header = new HBox(18);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBlock = new VBox(8);
        Label title = new Label("ByteSteps Lab");
        title.getStyleClass().add("app-title");
        title.setWrapText(true);

        Label subtitle = new Label("Μάθε Πληροφορική μέσα από θεωρία, μικρές προκλήσεις και επίπεδα.");
        subtitle.getStyleClass().add("app-subtitle");
        subtitle.setWrapText(true);
        titleBlock.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox scoreCards = new HBox(12);
        scoreCards.getChildren().addAll(
                scoreCard("Συνολικοί πόντοι", String.valueOf(totalPoints)),
                scoreCard("High score", String.valueOf(highScore))
        );

        header.getChildren().addAll(titleBlock, spacer, scoreCards);

        Label choose = new Label("Επίλεξε μάθημα");
        choose.getStyleClass().add("section-title");

        GridPane lessonsGrid = new GridPane();
        lessonsGrid.setHgap(22);
        lessonsGrid.setVgap(22);
        lessonsGrid.setAlignment(Pos.CENTER);

        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            VBox card = lessonCard(lesson, onLessonSelected);
            lessonsGrid.add(card, i % 2, i / 2);
        }

        page.getChildren().addAll(header, choose, lessonsGrid);
        setCenter(page);
    }

    private VBox scoreCard(String labelText, String valueText) {
        VBox card = new VBox(5);
        card.getStyleClass().add("score-card");
        card.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label(labelText);
        label.getStyleClass().add("score-label");
        Label value = new Label(valueText);
        value.getStyleClass().add("score-value");
        card.getChildren().addAll(label, value);
        return card;
    }

    private VBox lessonCard(Lesson lesson, Consumer<Lesson> onLessonSelected) {
        VBox card = new VBox(16);
        card.getStyleClass().addAll("lesson-card", lesson.getAccentClass());
        card.setAlignment(Pos.TOP_LEFT);
        card.setPrefSize(500, 210);
        card.setPadding(new Insets(24));
        card.setOnMouseClicked(event -> onLessonSelected.accept(lesson));

        Label title = new Label(lesson.getTitle());
        title.getStyleClass().add("lesson-title");
        title.setWrapText(true);
        title.setMaxWidth(Double.MAX_VALUE);

        Label description = new Label(lesson.getShortDescription());
        description.getStyleClass().add("lesson-description");
        description.setWrapText(true);
        description.setMaxWidth(Double.MAX_VALUE);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button start = new Button("Ξεκίνα μάθημα  →");
        start.getStyleClass().add("primary-button");
        start.setOnAction(event -> onLessonSelected.accept(lesson));

        card.getChildren().addAll(title, description, spacer, start);
        return card;
    }
}
