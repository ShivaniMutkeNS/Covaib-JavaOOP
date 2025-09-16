package composition.library;

import java.util.*;

/**
 * Physical Book Collection Implementation
 */
public class PhysicalBookCollection implements BookCollection {
    private final Map<String, Book> books;
    
    public PhysicalBookCollection() {
        this.books = new HashMap<>();
    }
    
    @Override
    public void addBook(String isbn, String title, String author) {
        books.put(isbn, new Book(isbn, title, author));
        System.out.println("Physical book added: " + title);
    }
    
    @Override
    public Book findBook(String isbn) {
        return books.get(isbn);
    }
    
    @Override
    public int getTotalBooks() {
        return books.size();
    }
    
    @Override
    public int getAvailableBooks() {
        return (int) books.values().stream().filter(Book::isAvailable).count();
    }
    
    @Override
    public String getType() {
        return "Physical Book Collection";
    }
}
