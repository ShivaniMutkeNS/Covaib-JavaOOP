package composition.library;

/**
 * Book entity class
 */
public class Book {
    private final String isbn;
    private final String title;
    private final String author;
    private boolean available;
    
    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.available = true;
    }
    
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    @Override
    public String toString() {
        return String.format("Book{isbn='%s', title='%s', author='%s', available=%s}", 
                           isbn, title, author, available);
    }
}
