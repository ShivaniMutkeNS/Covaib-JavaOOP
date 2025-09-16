package composition.library;

/**
 * MAANG-Level Demo: Library Management System
 * Demonstrates runtime composition swapping and flexibility
 */
public class LibraryDemo {
    public static void main(String[] args) {
        System.out.println("=== MAANG-Level Library Management System Demo ===\n");
        
        // Initial setup with physical books and basic membership
        Library library = new Library(
            "Central City Library",
            new PhysicalBookCollection(),
            new BasicMembershipSystem()
        );
        
        // Add some books
        library.addBook("978-0134685991", "Effective Java", "Joshua Bloch");
        library.addBook("978-0596009205", "Head First Design Patterns", "Eric Freeman");
        library.addBook("978-0321356680", "Effective C++", "Scott Meyers");
        
        library.displayLibraryStats();
        
        // Test borrowing with basic membership
        System.out.println("\n--- Testing Basic Membership ---");
        library.borrowBook("M001", "978-0134685991");
        library.borrowBook("M999", "978-0596009205"); // Invalid member
        
        library.displayLibraryStats();
        
        // Runtime composition change: Upgrade to premium membership
        System.out.println("\n--- Upgrading to Premium Membership System ---");
        library.setMembershipSystem(new PremiumMembershipSystem());
        
        library.borrowBook("P001", "978-0596009205");
        library.borrowBook("P001", "978-0321356680");
        
        // Runtime composition change: Switch to E-Book collection
        System.out.println("\n--- Switching to E-Book Collection ---");
        library.setBookCollection(new EBookCollection());
        
        // Add e-books (same ISBNs, different behavior)
        library.addBook("978-0134685991", "Effective Java (E-Book)", "Joshua Bloch");
        library.addBook("978-0596009205", "Head First Design Patterns (E-Book)", "Eric Freeman");
        library.addBook("978-1617294945", "Spring in Action (E-Book)", "Craig Walls");
        
        // Test e-book borrowing (unlimited availability)
        System.out.println("\n--- Testing E-Book Borrowing ---");
        library.borrowBook("P002", "978-0134685991");
        library.borrowBook("P003", "978-0134685991"); // Same book, different user
        
        library.displayLibraryStats();
        
        // Return books
        System.out.println("\n--- Returning Books ---");
        library.returnBook("M001", "978-0134685991");
        library.returnBook("P001", "978-0596009205");
        
        library.displayLibraryStats();
        
        System.out.println("\n=== Demo Complete: Library system adapted to different requirements without code changes ===");
    }
}
