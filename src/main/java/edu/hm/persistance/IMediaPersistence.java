package edu.hm.persistance;

import edu.hm.management.media.Book;
import edu.hm.management.media.Disc;
import edu.hm.management.media.Medium;

/**
 * Interface for the Media Persistance.
 * @author Daniel Gabl
 *
 */
public interface IMediaPersistence {
    
    /**
     * Method to clear the Library.
     */
    void clearLibary();
    
    /**
     * Persists a given book.
     * @param book Book to persist
     */
    void persistBook(Book book);
    
    /**
     * Persists a given disc.
     * @param disc Disc to persist
     */
    void persistDisc(Disc disc);
    
    /**
     * Deletes a given book from the database.
     * @param book Book to persist
     */
    Medium deleteBook(Book book);
    
    /**
     * Deletes a given disc from the database.
     * @param disc Disc to persist
     */
    Medium deleteDisc(Disc disc);
    
    /**
     * Returns all Books of our database.
     * @return all Books of our database
     */
    Book[] getBooks();
    
    /**
     * Returns all Discs of our database.
     * @return all Discs of our database
     */
    Disc[] getDiscs();
    
    /**
     * Returns a Book of database.
     * @param isbn ISBN of Book to find.
     * @return a Book of our database.
     */
    Medium findBook(String isbn);
    
    /**
     * Returns a Disc of our database.
     * @param barcode Barcode of Disc to find.
     * @return a Disc of our database
     */
    Medium findDisc(String barcode);
}
