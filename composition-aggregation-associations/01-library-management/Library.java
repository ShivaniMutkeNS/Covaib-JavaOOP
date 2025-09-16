package composition.library;

/**
 * MAANG-Level Library Management System using Composition
 * Demonstrates: Strategy Pattern, Dependency Injection, Runtime Flexibility
 */
public class Library {
    private BookCollection bookCollection;
    private MembershipSystem membershipSystem;
    private final String libraryName;
    
    public Library(String libraryName, BookCollection bookCollection, MembershipSystem membershipSystem) {
        this.libraryName = libraryName;
        this.bookCollection = bookCollection;
        this.membershipSystem = membershipSystem;
    }
    
    // Dynamic composition - can swap implementations at runtime
    public void setBookCollection(BookCollection bookCollection) {
        this.bookCollection = bookCollection;
        System.out.println("Library: Book collection system updated to " + bookCollection.getType());
    }
    
    public void setMembershipSystem(MembershipSystem membershipSystem) {
        this.membershipSystem = membershipSystem;
        System.out.println("Library: Membership system updated to " + membershipSystem.getType());
    }
    
    public void addBook(String isbn, String title, String author) {
        bookCollection.addBook(isbn, title, author);
    }
    
    public Book findBook(String isbn) {
        return bookCollection.findBook(isbn);
    }
    
    public boolean borrowBook(String memberId, String isbn) {
        if (!membershipSystem.isValidMember(memberId)) {
            System.out.println("Invalid member ID: " + memberId);
            return false;
        }
        
        Book book = bookCollection.findBook(isbn);
        if (book != null && book.isAvailable()) {
            book.setAvailable(false);
            membershipSystem.recordBorrow(memberId, isbn);
            System.out.println("Book borrowed successfully: " + book.getTitle());
            return true;
        }
        
        System.out.println("Book not available: " + isbn);
        return false;
    }
    
    public void returnBook(String memberId, String isbn) {
        Book book = bookCollection.findBook(isbn);
        if (book != null) {
            book.setAvailable(true);
            membershipSystem.recordReturn(memberId, isbn);
            System.out.println("Book returned successfully: " + book.getTitle());
        }
    }
    
    public void displayLibraryStats() {
        System.out.println("\n=== " + libraryName + " Statistics ===");
        System.out.println("Collection Type: " + bookCollection.getType());
        System.out.println("Total Books: " + bookCollection.getTotalBooks());
        System.out.println("Available Books: " + bookCollection.getAvailableBooks());
        System.out.println("Membership System: " + membershipSystem.getType());
        System.out.println("Total Members: " + membershipSystem.getTotalMembers());
    }
}
