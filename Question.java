import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Question {
    private final QuestionType type;
    private final String prompt;
    private final List<String> options;
    private final List<String> correctAnswers;
    private final String explanation;

    public Question(QuestionType type,
                    String prompt,
                    List<String> options,
                    List<String> correctAnswers,
                    String explanation) {
        this.type = type;
        this.prompt = prompt;
        this.options = options == null ? List.of() : new ArrayList<>(options);
        this.correctAnswers = correctAnswers == null ? List.of() : new ArrayList<>(correctAnswers);
        this.explanation = explanation;
    }

    public QuestionType getType() {
        return type;
    }

    public String getPrompt() {
        return prompt;
    }

    public List<String> getOptions() {
        return Collections.unmodifiableList(options);
    }

    public List<String> getCorrectAnswers() {
        return Collections.unmodifiableList(correctAnswers);
    }

    public String getPrimaryCorrectAnswer() {
        if (correctAnswers.isEmpty()) {
            return "";
        }
        return correctAnswers.get(0);
    }

    public String getExplanation() {
        return explanation;
    }

    public boolean isCorrect(String userAnswer) {
        if (userAnswer == null || userAnswer.isBlank()) {
            return false;
        }
        String normalizedUserAnswer = normalize(userAnswer);
        return correctAnswers.stream()
                .map(Question::normalize)
                .anyMatch(correct -> correct.equals(normalizedUserAnswer));
    }

    private static String normalize(String text) {
        String lower = text.trim().toLowerCase(Locale.ROOT);
        String withoutAccents = Normalizer.normalize(lower, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return withoutAccents.replaceAll("\\s+", " ");
    }
}
