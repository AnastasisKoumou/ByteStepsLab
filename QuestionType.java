public enum QuestionType {
    MULTIPLE_CHOICE("Πολλαπλής επιλογής"),
    TRUE_FALSE("Σωστό / Λάθος"),
    FILL_BLANK("Συμπλήρωση κενού");

    private final String label;

    QuestionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
