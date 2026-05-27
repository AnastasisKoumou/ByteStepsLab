import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ScoreManager {
    private static final String TOTAL_POINTS = "totalPoints";
    private static final String HIGH_SCORE = "highScore";

    private final Path saveFile;
    private final Properties properties;

    public ScoreManager() {
        this.saveFile = Path.of(System.getProperty("user.home"), ".bytesteps-lab.properties");
        this.properties = new Properties();
        load();
    }

    public int getTotalPoints() {
        return getInt(TOTAL_POINTS);
    }

    public int getHighScore() {
        return getInt(HIGH_SCORE);
    }

    public int addPoints(int points) {
        int updatedTotal = Math.max(0, getTotalPoints() + points);
        properties.setProperty(TOTAL_POINTS, String.valueOf(updatedTotal));

        if (updatedTotal > getHighScore()) {
            properties.setProperty(HIGH_SCORE, String.valueOf(updatedTotal));
        }

        save();
        return updatedTotal;
    }

    public void resetAll() {
        properties.setProperty(TOTAL_POINTS, "0");
        properties.setProperty(HIGH_SCORE, "0");
        save();
    }

    private void load() {
        if (Files.exists(saveFile)) {
            try (InputStream input = Files.newInputStream(saveFile)) {
                properties.load(input);
            } catch (IOException ignored) {
                setDefaults();
            }
        } else {
            setDefaults();
        }
    }

    private void setDefaults() {
        properties.setProperty(TOTAL_POINTS, "0");
        properties.setProperty(HIGH_SCORE, "0");
    }

    private void save() {
        try (OutputStream output = Files.newOutputStream(saveFile)) {
            properties.store(output, "ByteSteps Lab scores");
        } catch (IOException ignored) {
            // Αν δεν μπορεί να γραφτεί το αρχείο, το παιχνίδι συνεχίζει κανονικά στη μνήμη.
        }
    }

    private int getInt(String key) {
        try {
            return Integer.parseInt(properties.getProperty(key, "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
