public class Worksheet {
    private final String title;
    private final String theory;
    private final Question question;

    public Worksheet(String title, String theory, Question question) {
        this.title = title;
        this.theory = theory;
        this.question = question;
    }

    public String getTitle() {
        return title;
    }

    public String getTheory() {
        return theory;
    }

    public Question getQuestion() {
        return question;
    }
}
