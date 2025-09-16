import java.time.LocalDateTime;

public class FileLogger implements Logger {
	@Override
	public void log(String message) {
		String line = LocalDateTime.now() + " | " + message;
		System.out.println("[FILE] " + line);
	}
}


