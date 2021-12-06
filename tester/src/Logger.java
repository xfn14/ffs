import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class Logger {
    private java.util.logging.Logger logger = java.util.logging.Logger.getLogger("FFSync");
    private FileHandler fileHandler;
    private SimpleFormatter formatter = new SimpleFormatter();

    public Logger() {
        try {
            this.fileHandler = new FileHandler("log.txt");
            this.logger.addHandler(fileHandler);
            this.fileHandler.setFormatter(this.formatter);
        } catch (IOException e) {
            this.logger.warning("Failed to open log file. Logs won't be registered.");
        }
    }

    public void log(Level level, String message){
        this.logger.log(level, message);
    }
}
