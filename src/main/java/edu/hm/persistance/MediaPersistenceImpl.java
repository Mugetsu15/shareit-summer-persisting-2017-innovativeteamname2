package edu.hm.persistance;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.inject.Inject;
import edu.hm.management.media.Book;
import edu.hm.management.media.Disc;
import edu.hm.management.media.Medium;

/**
 * Implementation for Media Service Interface.
 * @author Daniel Gabl
 *
 */
public class MediaPersistenceImpl implements IMediaPersistence {
    
    @Inject
    private SessionFactory sessionFactory;
    
    /**
     * Method to clear the database.
     */
    public void clearLibary()  {
    }
    
    /**
     * Default Constructor.
     */
    public MediaPersistenceImpl()  {
    }

    /**
     * Persisting given Data.
     * @param obj Object to persist.
     */
    private void persist(Object obj) {
        Session entityManager = sessionFactory.getCurrentSession();
        Transaction tx = entityManager.beginTransaction();
        entityManager.persist(obj);
        tx.commit();
    }
    
    /**
     * Deleting persisted Data.
     * @param obj Object to delete.
     */
    private void delete(Object obj) {
        Session entityManager = sessionFactory.getCurrentSession();
        Transaction tx = entityManager.beginTransaction();
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
    public Book[] getBooks() {
        Session entityManager = sessionFactory.getCurrentSession();
        Transaction tx = entityManager.beginTransaction();
        
        String tablename = Medium.getTableName(); // TableMedium
        
        String queryString = String.format("from %s", tablename);
        System.out.println(String.format("Query: '%s'", queryString));
        Query<Book> query = entityManager.createQuery(queryString);
        tx.commit();
        List<Book> books = query.list();
        
        Book[] media = new Book[books.size()];
        media = books.toArray(media);
        return media;
    }

    @Override
    public Disc[] getDiscs() {
        Session entityManager = sessionFactory.getCurrentSession();
        
        String tablename = Medium.getTableName();
        
        String queryString = String.format("from %s", tablename);
        Query<Disc> query = entityManager.createQuery(queryString);
        List<Disc> discs = query.list();
        
        Disc[] media = new Disc[discs.size()];
        return media;
    }

    @Override
    public Medium findBook(String isbn) {
        Session entityManager = sessionFactory.getCurrentSession();
    
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
        Session entityManager = sessionFactory.getCurrentSession();
        
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Disc> query = builder.createQuery(Disc.class);
        Root<Disc> root = query.from(Disc.class);
        query.where(builder.equal(root.get("barcode"), barcode));
        Query<Disc> q = entityManager.createQuery(query);
        Disc disc = q.getResultList().get(0);
        
        return disc;
    }
}
