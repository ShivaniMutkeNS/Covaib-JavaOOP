public class CloudLogger implements Logger {
	@Override
	public void log(String message) {
		System.out.println("[CLOUD] " + message);
	}
}


