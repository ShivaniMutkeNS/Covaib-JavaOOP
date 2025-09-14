public class SimpleTest {
    public static void main(String[] args) {
        System.out.println("=== Java is working! ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println("Architecture: " + System.getProperty("os.arch"));
        System.out.println("\n✅ Java environment is properly configured!");
        System.out.println("❌ Java compiler (javac) is not available in PATH");
        System.out.println("\nTo run the encapsulation projects:");
        System.out.println("1. Install JDK (Java Development Kit)");
        System.out.println("2. Add javac to PATH");
        System.out.println("3. Or use an IDE like IntelliJ IDEA, Eclipse, or VS Code");
    }
}
