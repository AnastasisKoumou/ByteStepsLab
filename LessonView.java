import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LessonView extends BorderPane {
    private LessonSession session;
    private final ScoreManager scoreManager;
    private final Runnable onBackHome;

    private Label levelLabel;
    private Label scoreLabel;
    private Label wrongsHeaderLabel;
    private Label attemptsLabel;
    private ProgressBar progressBar;
    private VBox theoryBox;
    private VBox exerciseBox;

    private ToggleGroup answerGroup;
    private TextField answerField;

    public LessonView(LessonSession session, ScoreManager scoreManager, Runnable onBackHome) {
        this.session = session;
        this.scoreManager = scoreManager;
        this.onBackHome = onBackHome;
        buildLayout();
        refreshWorksheet();
    }

    private void buildLayout() {
        getStyleClass().setAll("app-root");
        setPadding(new Insets(26));

        HBox topBar = new HBox(18);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getStyleClass().add("top-bar");

        Button backButton = new Button("← Αρχικό μενού");
        backButton.getStyleClass().add("ghost-button");
        backButton.setOnAction(event -> onBackHome.run());

        Label lessonTitle = new Label(session.getLesson().getTitle());
        lessonTitle.getStyleClass().add("lesson-page-title");
        lessonTitle.setWrapText(true);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        levelLabel = new Label();
        levelLabel.getStyleClass().add("pill");
        wrongsHeaderLabel = new Label();
        wrongsHeaderLabel.getStyleClass().add("pill");
        scoreLabel = new Label();
        scoreLabel.getStyleClass().add("pill");

        topBar.getChildren().addAll(backButton, lessonTitle, spacer, levelLabel, wrongsHeaderLabel, scoreLabel);

        progressBar = new ProgressBar(0);
        progressBar.getStyleClass().add("progress");
        progressBar.setMaxWidth(Double.MAX_VALUE);

        VBox header = new VBox(12, topBar, progressBar);
        setTop(header);

        theoryBox = new VBox(18);
        theoryBox.getStyleClass().add("content-card");
        theoryBox.setPadding(new Insets(24));
        theoryBox.setMaxWidth(Double.MAX_VALUE);

        ScrollPane theoryScroll = new ScrollPane(theoryBox);
        theoryScroll.getStyleClass().add("clean-scroll");
        theoryScroll.setFitToWidth(true);
        theoryScroll.setFitToHeight(false);
        theoryScroll.setMaxWidth(Double.MAX_VALUE);

        exerciseBox = new VBox(18);
        exerciseBox.getStyleClass().add("content-card");
        exerciseBox.setPadding(new Insets(24));
        exerciseBox.setMaxWidth(Double.MAX_VALUE);

        ScrollPane exerciseScroll = new ScrollPane(exerciseBox);
        exerciseScroll.getStyleClass().add("clean-scroll");
        exerciseScroll.setFitToWidth(true);
        exerciseScroll.setFitToHeight(false);
        exerciseScroll.setMaxWidth(Double.MAX_VALUE);

        HBox main = new HBox(22, theoryScroll, exerciseScroll);
        main.setPadding(new Insets(24, 0, 0, 0));
        main.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(theoryScroll, Priority.ALWAYS);
        HBox.setHgrow(exerciseScroll, Priority.ALWAYS);
        theoryScroll.prefWidthProperty().bind(main.widthProperty().subtract(22).divide(2));
        exerciseScroll.prefWidthProperty().bind(main.widthProperty().subtract(22).divide(2));

        setCenter(main);
    }

    private void refreshWorksheet() {
        if (session.isFinished()) {
            showFinishScreen();
            return;
        }

        answerGroup = null;
        answerField = null;

        Worksheet worksheet = session.getCurrentWorksheet();
        Question question = worksheet.getQuestion();

        theoryBox.getChildren().clear();
        exerciseBox.getChildren().clear();

        Label worksheetLabel = new Label("Φύλλο εργασίας " + session.getCurrentLevel() + " / " + session.getTotalLevels());
        worksheetLabel.getStyleClass().add("eyebrow");
        worksheetLabel.setWrapText(true);
        worksheetLabel.setMaxWidth(Double.MAX_VALUE);

        Label title = new Label(worksheet.getTitle());
        title.getStyleClass().add("worksheet-title");
        title.setWrapText(true);
        title.setMaxWidth(Double.MAX_VALUE);

        Label theoryTitle = new Label("Θεωρία");
        theoryTitle.getStyleClass().add("mini-title");
        theoryTitle.setMaxWidth(Double.MAX_VALUE);

        Label theoryText = new Label(worksheet.getTheory());
        theoryText.getStyleClass().add("theory-text");
        theoryText.setWrapText(true);
        theoryText.setMaxWidth(Double.MAX_VALUE);

        theoryBox.getChildren().addAll(worksheetLabel, title, theoryTitle, theoryText);

        Label exerciseTitle = new Label("Άσκηση");
        exerciseTitle.getStyleClass().add("mini-title");
        exerciseTitle.setMaxWidth(Double.MAX_VALUE);

        Label type = new Label(question.getType().getLabel());
        type.getStyleClass().add("question-type");
        type.setWrapText(true);

        Label prompt = new Label(question.getPrompt());
        prompt.getStyleClass().add("question-prompt");
        prompt.setWrapText(true);
        prompt.setMaxWidth(Double.MAX_VALUE);

        Node answerNode = createAnswerNode(question);

        attemptsLabel = new Label();
        attemptsLabel.getStyleClass().add("attempts-label");
        attemptsLabel.setWrapText(true);
        attemptsLabel.setMaxWidth(Double.MAX_VALUE);

        Label feedback = new Label();
        feedback.getStyleClass().add("feedback");
        feedback.setWrapText(true);
        feedback.setMaxWidth(Double.MAX_VALUE);

        Button checkButton = new Button("Έλεγχος απάντησης");
        checkButton.getStyleClass().add("primary-button");
        checkButton.setMaxWidth(Double.MAX_VALUE);
        checkButton.setOnAction(event -> checkAnswer(question, feedback, checkButton));

        exerciseBox.getChildren().addAll(exerciseTitle, type, prompt, answerNode, attemptsLabel, checkButton, feedback);
        updateHeader();
    }

    private Node createAnswerNode(Question question) {
        if (question.getType() == QuestionType.FILL_BLANK) {
            answerField = new TextField();
            answerField.getStyleClass().add("answer-field");
            answerField.setPromptText("Πληκτρολόγησε την απάντηση...");
            answerField.setMaxWidth(Double.MAX_VALUE);
            return answerField;
        }

        answerGroup = new ToggleGroup();
        VBox optionsBox = new VBox(10);
        optionsBox.getStyleClass().add("options-box");
        optionsBox.setMaxWidth(Double.MAX_VALUE);

        List<String> options = new ArrayList<>();
        if (question.getType() == QuestionType.TRUE_FALSE) {
            options.add("Σωστό");
            options.add("Λάθος");
        } else {
            options.addAll(question.getOptions());
            Collections.shuffle(options);
        }

        for (String option : options) {
            RadioButton radioButton = new RadioButton(option);
            radioButton.getStyleClass().add("option-radio");
            radioButton.setToggleGroup(answerGroup);
            radioButton.setUserData(question.getType() == QuestionType.TRUE_FALSE
                    ? (option.equals("Σωστό") ? "true" : "false")
                    : option);
            radioButton.setWrapText(true);
            radioButton.setMaxWidth(Double.MAX_VALUE);
            optionsBox.getChildren().add(radioButton);
        }

        return optionsBox;
    }

    private void checkAnswer(Question question, Label feedback, Button checkButton) {
        String answer = readUserAnswer(question);
        if (answer == null || answer.isBlank()) {
            feedback.setText("Διάλεξε ή συμπλήρωσε πρώτα μία απάντηση.");
            feedback.getStyleClass().removeAll("feedback-success", "feedback-error");
            feedback.getStyleClass().add("feedback-error");
            return;
        }

        boolean correct = question.isCorrect(answer);
        checkButton.setDisable(true);
        lockAnswers();

        if (correct) {
            scoreManager.addPoints(10);
            markCorrectAnswer(question, answer);
            feedback.setText("Σωστά! +10 πόντοι. " + question.getExplanation());
            feedback.getStyleClass().removeAll("feedback-error");
            feedback.getStyleClass().add("feedback-success");
            addNextButton();
        } else {
            scoreManager.addPoints(-10);
            int wrongAttempts = session.addWrongAttempt();
            markWrongAnswer(question, answer);
            showCorrectAnswerForFillBlankIfNeeded(question);

            feedback.getStyleClass().removeAll("feedback-success", "feedback-error");
            feedback.getStyleClass().add("feedback-error");
            feedback.setText("Λάθος απάντηση. -10 πόντοι. Η σωστή απάντηση έχει επισημανθεί. " + question.getExplanation());

            if (wrongAttempts >= LessonSession.MAX_WRONG_ATTEMPTS_PER_LESSON) {
                showAttemptLimitMessage();
            } else {
                addNextButton();
            }
        }

        updateHeader();
    }

    private String readUserAnswer(Question question) {
        if (question.getType() == QuestionType.FILL_BLANK) {
            return answerField.getText();
        }
        if (answerGroup.getSelectedToggle() == null) {
            return null;
        }
        return String.valueOf(answerGroup.getSelectedToggle().getUserData());
    }

    private void lockAnswers() {
        if (answerField != null) {
            answerField.setEditable(false);
            answerField.setMouseTransparent(true);
            answerField.setFocusTraversable(false);
        }
        if (answerGroup != null) {
            answerGroup.getToggles().forEach(toggle -> {
                RadioButton radioButton = (RadioButton) toggle;
                radioButton.setMouseTransparent(true);
                radioButton.setFocusTraversable(false);
            });
        }
    }

    private void markCorrectAnswer(Question question, String selectedAnswer) {
        if (question.getType() == QuestionType.FILL_BLANK) {
            answerField.getStyleClass().removeAll("answer-wrong", "answer-correct");
            answerField.getStyleClass().add("answer-correct");
            return;
        }

        markRadioAnswers(question, selectedAnswer);
    }

    private void markWrongAnswer(Question question, String selectedAnswer) {
        if (question.getType() == QuestionType.FILL_BLANK) {
            answerField.getStyleClass().removeAll("answer-wrong", "answer-correct");
            answerField.getStyleClass().add("answer-wrong");
            return;
        }

        markRadioAnswers(question, selectedAnswer);
    }

    private void markRadioAnswers(Question question, String selectedAnswer) {
        if (answerGroup == null) {
            return;
        }

        answerGroup.getToggles().forEach(toggle -> {
            RadioButton radioButton = (RadioButton) toggle;
            String optionValue = String.valueOf(radioButton.getUserData());
            boolean isCorrectOption = question.isCorrect(optionValue);
            boolean isSelectedWrongOption = optionValue.equals(selectedAnswer) && !isCorrectOption;

            radioButton.getStyleClass().removeAll("option-correct", "option-wrong");
            if (isCorrectOption) {
                radioButton.getStyleClass().add("option-correct");
            } else if (isSelectedWrongOption) {
                radioButton.getStyleClass().add("option-wrong");
            }
        });
    }

    private void showCorrectAnswerForFillBlankIfNeeded(Question question) {
        if (question.getType() != QuestionType.FILL_BLANK) {
            return;
        }

        Label correctAnswer = new Label("Σωστή απάντηση: " + question.getPrimaryCorrectAnswer());
        correctAnswer.getStyleClass().add("correct-answer-label");
        correctAnswer.setWrapText(true);
        correctAnswer.setMaxWidth(Double.MAX_VALUE);
        exerciseBox.getChildren().add(correctAnswer);
    }

    private void addNextButton() {
        Button nextButton = new Button(session.getCurrentLevel() == session.getTotalLevels()
                ? "Ολοκλήρωση μαθήματος"
                : "Επόμενο επίπεδο  →");
        nextButton.getStyleClass().add("primary-button");
        nextButton.setMaxWidth(Double.MAX_VALUE);
        nextButton.setOnAction(event -> {
            session.advance();
            refreshWorksheet();
        });
        exerciseBox.getChildren().add(nextButton);
    }

    private void showAttemptLimitMessage() {
        VBox message = new VBox(14);
        message.getStyleClass().add("failure-card");
        message.setAlignment(Pos.CENTER_LEFT);
        message.setMaxWidth(Double.MAX_VALUE);

        Label title = new Label("Τέλος προσπαθειών");
        title.getStyleClass().add("failure-title");
        title.setWrapText(true);

        Label text = new Label("Έκανες 3 λάθη συνολικά σε αυτή τη θεματική ενότητα. Μπορείς να επιστρέψεις στο αρχικό μενού ή να ξεκινήσεις ξανά την ίδια ενότητα με νέα σειρά ερωτήσεων.");
        text.getStyleClass().add("failure-text");
        text.setWrapText(true);
        text.setMaxWidth(Double.MAX_VALUE);

        Button restart = new Button("Restart θεματικής ενότητας");
        restart.getStyleClass().add("primary-button");
        restart.setMaxWidth(Double.MAX_VALUE);
        restart.setOnAction(event -> {
            this.session = new LessonSession(session.getLesson());
            buildLayout();
            refreshWorksheet();
        });

        Button home = new Button("Επιστροφή στο αρχικό μενού");
        home.getStyleClass().add("ghost-button");
        home.setMaxWidth(Double.MAX_VALUE);
        home.setOnAction(event -> onBackHome.run());

        HBox actions = new HBox(12, restart, home);
        actions.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(restart, Priority.ALWAYS);
        HBox.setHgrow(home, Priority.ALWAYS);

        message.getChildren().addAll(title, text, actions);
        exerciseBox.getChildren().add(message);
    }

    private void updateHeader() {
        levelLabel.setText("Επίπεδο " + Math.min(session.getCurrentLevel(), session.getTotalLevels()) + " / " + session.getTotalLevels());
        wrongsHeaderLabel.setText("Λάθη: " + session.getWrongAttempts() + " / " + LessonSession.MAX_WRONG_ATTEMPTS_PER_LESSON);
        scoreLabel.setText("Πόντοι: " + scoreManager.getTotalPoints());
        progressBar.setProgress(session.getProgress());

        if (attemptsLabel != null) {
            attemptsLabel.setText("Λάθη θεματικής ενότητας: " + session.getWrongAttempts()
                    + " / " + LessonSession.MAX_WRONG_ATTEMPTS_PER_LESSON
                    + " | Απομένουν: " + session.getRemainingAttempts());
        }
    }

    private void showFinishScreen() {
        updateHeader();
        progressBar.setProgress(1.0);

        VBox finish = new VBox(20);
        finish.setAlignment(Pos.CENTER);
        finish.getStyleClass().add("finish-card");
        finish.setPadding(new Insets(50));

        Label trophy = new Label("🏆");
        trophy.getStyleClass().add("finish-icon");

        Label title = new Label("Ολοκλήρωσες το μάθημα!");
        title.getStyleClass().add("finish-title");
        title.setWrapText(true);

        Label text = new Label("Συνολικοί πόντοι: " + scoreManager.getTotalPoints() + " | High score: " + scoreManager.getHighScore());
        text.getStyleClass().add("finish-text");
        text.setWrapText(true);

        Button restart = new Button("Ξανά με νέα σειρά ερωτήσεων");
        restart.getStyleClass().add("primary-button");
        restart.setOnAction(event -> {
            this.session = new LessonSession(session.getLesson());
            buildLayout();
            refreshWorksheet();
        });

        Button home = new Button("Επιστροφή στο αρχικό μενού");
        home.getStyleClass().add("ghost-button");
        home.setOnAction(event -> onBackHome.run());

        HBox actions = new HBox(12, restart, home);
        actions.setAlignment(Pos.CENTER);

        finish.getChildren().addAll(trophy, title, text, actions);
        setCenter(finish);
    }
}
