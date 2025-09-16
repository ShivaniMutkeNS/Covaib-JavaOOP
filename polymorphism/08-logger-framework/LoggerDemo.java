public class LoggerDemo {
	public static void main(String[] args) {
		Logger[] loggers = new Logger[] {
			new ConsoleLogger(),
			new FileLogger(),
			new CloudLogger()
		};

		for (Logger logger : loggers) {
			logger.log("Polymorphic logging with " + logger.getName());
		}
	}
}


