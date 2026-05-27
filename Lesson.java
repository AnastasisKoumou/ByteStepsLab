import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lesson {
    private final String id;
    private final String title;
    private final String shortDescription;
    private final String icon;
    private final String accentClass;
    private final List<Worksheet> worksheets;

    public Lesson(String id, String title, String shortDescription, String icon, String accentClass, List<Worksheet> worksheets) {
        this.id = id;
        this.title = title;
        this.shortDescription = shortDescription;
        this.icon = icon;
        this.accentClass = accentClass;
        this.worksheets = new ArrayList<>(worksheets);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getIcon() {
        return icon;
    }

    public String getAccentClass() {
        return accentClass;
    }

    public List<Worksheet> getWorksheets() {
        return Collections.unmodifiableList(worksheets);
    }
}
