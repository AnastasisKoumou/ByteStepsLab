import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Stage primaryStage;
    private Scene appScene;
    private ScoreManager scoreManager;
    private LessonRepository lessonRepository;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.scoreManager = new ScoreManager();
        this.lessonRepository = new LessonRepository();

        appScene = createScene(new StackPane());
        primaryStage.setScene(appScene);
        primaryStage.setTitle("ByteSteps Lab - Διδακτική της Πληροφορικής");
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(720);

        showHome();
        primaryStage.show();
    }

    private void showHome() {
        HomeView homeView = new HomeView(
                lessonRepository.getLessons(),
                scoreManager.getTotalPoints(),
                scoreManager.getHighScore(),
                this::showLesson
        );
        setRootWithoutLeavingFullscreen(homeView);
    }

    private void showLesson(Lesson lesson) {
        LessonSession session = new LessonSession(lesson);
        LessonView lessonView = new LessonView(session, scoreManager, this::showHome);
        setRootWithoutLeavingFullscreen(lessonView);
    }

    private Scene createScene(Parent root) {
        Scene scene = new Scene(root, 1180, 760);
        var cssUrl = getClass().getResource("/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
        return scene;
    }

    private void setRootWithoutLeavingFullscreen(Parent root) {
        boolean wasFullscreen = primaryStage.isFullScreen();
        appScene.setRoot(root);
        if (wasFullscreen && !primaryStage.isFullScreen()) {
            primaryStage.setFullScreen(true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
