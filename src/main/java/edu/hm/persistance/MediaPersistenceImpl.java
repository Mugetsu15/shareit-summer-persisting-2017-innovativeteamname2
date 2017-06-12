package edu.hm.persistance;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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
     * Method to clear the database.
     */
    public void clearLibary()  {
    }
    
    /**
     * Default Constructor.
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
     * Deleting persisted Data.
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
    public Medium deleteBook(Book book) {
       delete(book);
       return book;
        
    }

    @Override
    public Medium deleteDisc(Disc disc) {
       delete(disc);
       return disc;
        
    }
    
    
    @Override
    public Medium[] getBooks() {
        String queryString = "from TableMedium where isbn is not null";
        Query<Book> query = entityManager.createQuery(queryString);
        List<Book> books = query.list();
        
        Book[] media = new Book[books.size()];
        media = books.toArray(media);
        return media;
    }

    @Override
    public Medium[] getDiscs() {
        String queryString = "from TableMedium where barcode is not null";
        Query<Disc> query = entityManager.createQuery(queryString);
        List<Disc> discs = query.list();
        
        Disc[] media = new Disc[discs.size()];
        return media;
    }

    @Override
    public Medium findBook(String isbn) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = builder.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);
        query.where(builder.equal(root.get("isbn"), isbn));
        Query<Book> q = entityManager.createQuery(query);
        Book book = q.getResultList().get(0);
        
        return book;
    }

    @Override
    public Medium findDisc(String barcode) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Disc> query = builder.createQuery(Disc.class);
        Root<Disc> root = query.from(Disc.class);
        query.where(builder.equal(root.get("barcode"), barcode));
        Query<Disc> q = entityManager.createQuery(query);
        Disc disc = q.getResultList().get(0);
        
        return disc;
    }

}
