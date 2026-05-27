import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LessonSession {
    public static final int MAX_WRONG_ATTEMPTS_PER_LESSON = 3;

    private final Lesson lesson;
    private final List<Worksheet> randomizedWorksheets;
    private int currentIndex;
    private int wrongAttemptsInLesson;

    public LessonSession(Lesson lesson) {
        this.lesson = lesson;
        this.randomizedWorksheets = new ArrayList<>(lesson.getWorksheets());
        Collections.shuffle(this.randomizedWorksheets);
        this.currentIndex = 0;
        this.wrongAttemptsInLesson = 0;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public Worksheet getCurrentWorksheet() {
        if (isFinished()) {
            return null;
        }
        return randomizedWorksheets.get(currentIndex);
    }

    public int getCurrentLevel() {
        return currentIndex + 1;
    }

    public int getTotalLevels() {
        return randomizedWorksheets.size();
    }

    public double getProgress() {
        return (double) currentIndex / randomizedWorksheets.size();
    }

    public boolean isFinished() {
        return currentIndex >= randomizedWorksheets.size();
    }

    public void advance() {
        currentIndex++;
    }

    public int addWrongAttempt() {
        wrongAttemptsInLesson++;
        return wrongAttemptsInLesson;
    }

    public int getWrongAttempts() {
        return wrongAttemptsInLesson;
    }

    public int getRemainingAttempts() {
        return Math.max(0, MAX_WRONG_ATTEMPTS_PER_LESSON - wrongAttemptsInLesson);
    }

    public boolean hasFailedLesson() {
        return wrongAttemptsInLesson >= MAX_WRONG_ATTEMPTS_PER_LESSON;
    }
}
