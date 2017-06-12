package edu.hm.persistance;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import edu.hm.GuiceModule;
import edu.hm.management.media.Book;
import edu.hm.management.media.Disc;
import edu.hm.management.media.Medium;

/**
 * Implementation for Media Service Interface.
 * @author Daniel Gabl
 *
 */
public class MediaPersistenceImpl implements IMediaPersistence {
    
    private static final Injector injector = Guice.createInjector(new GuiceModule());
    
    @Inject
    private SessionFactory sessionFactory;
    private Session entityManager;
    private Transaction tx;
    
    /**
     * List to save all Books.
     */
    private static List<Book> books = new ArrayList<>();
    
    /**
     * List to save all Discs.
     */
    private static List<Disc> discs = new ArrayList<>();
    
    /**
     * Method to clear the Library.
     */
    public void clearLibary()  {
        books.clear();
        discs.clear();
    }
    
    /**
     * Default Constructor, only for Jackson.
     */
    public MediaPersistenceImpl()  {
        injector.injectMembers(this);
        
    }

    /**
     * Persisting given Data.
     * @param obj Object to persist.
     */
    private void persist(Object obj) {
        entityManager = sessionFactory.getCurrentSession();
        tx = entityManager.beginTransaction();
        entityManager.persist(obj);
        tx.commit();
    }
    
    /**
     * Deleting persisted Data..
     * @param obj Object to delete.
     */
    private void delete(Object obj) {
        entityManager = sessionFactory.getCurrentSession();
        tx = entityManager.beginTransaction();
        entityManager.delete(obj);
        tx.commit();
    }

    @Override
    public void persistBook(Book book) {
        persist(book);
        
    }

    @Override
    public void persistDisc(Disc disc) {
        persist(disc);        
    }
    

    @Override
    public Book deleteBook(Book book) {
       delete(book);
       return book;
        
    }

    @Override
    public Disc deleteDisc(Disc disc) {
       delete(disc);
       return disc;
        
    }
    
    @Override
    public Medium[] getBooks() {
        Book[] media = new Book[books.size()];
        media = books.toArray(media);
        return media;
    }

    @Override
    public Medium[] getDiscs() {
        Disc[] media = new Disc[discs.size()];
        media = discs.toArray(media);
        return media;
    }

    @Override
    public Medium findBook(String isbn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Medium findDisc(String barcode) {
        // TODO Auto-generated method stub
        return null;
    }

}
