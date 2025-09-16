package composition.library;

import java.util.*;

/**
 * Electronic Book Collection Implementation with cloud features
 */
public class EBookCollection implements BookCollection {
    private final Map<String, Book> books;
    private final Set<String> downloadedBooks;
    
    public EBookCollection() {
        this.books = new HashMap<>();
        this.downloadedBooks = new HashSet<>();
    }
    
    @Override
    public void addBook(String isbn, String title, String author) {
        books.put(isbn, new Book(isbn, title, author));
        System.out.println("E-book added to cloud library: " + title);
    }
    
    @Override
    public Book findBook(String isbn) {
        Book book = books.get(isbn);
        if (book != null && !downloadedBooks.contains(isbn)) {
            downloadedBooks.add(isbn);
            System.out.println("E-book downloaded: " + book.getTitle());
        }
        return book;
    }
    
    @Override
    public int getTotalBooks() {
        return books.size();
    }
    
    @Override
    public int getAvailableBooks() {
        // E-books are always available (unlimited copies)
        return books.size();
    }
    
    @Override
    public String getType() {
        return "Electronic Book Collection (Cloud)";
    }
}
