package composition.library;

/**
 * Abstract BookCollection interface for Strategy Pattern
 */
public interface BookCollection {
    void addBook(String isbn, String title, String author);
    Book findBook(String isbn);
    int getTotalBooks();
    int getAvailableBooks();
    String getType();
}
