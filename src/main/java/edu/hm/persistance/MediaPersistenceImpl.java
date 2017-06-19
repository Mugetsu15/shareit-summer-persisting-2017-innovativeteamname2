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
     * Default Constructor.
     */
    public MediaPersistenceImpl()  {
    }
    
    /**
     * Method to clear the database.
     */
    public void clearLibary()  {
    }
    
    
    /**
     * Method to initialize data from the database. Should be happening automatically.
     */
//    private void initialize()  {
//        // Session entityManager = sessionFactory.getCurrentSession();
//        
//        // Get all books and discs
//        Book[] books = getBooks();
//        Disc[] discs = getDiscs();
//        
//        //Transaction tx = entityManager.beginTransaction();
//        
//        int minSize = 0;
//        boolean moreBooks;
//        boolean equalObjects;
//        
//        // determine if there are more books or discs
//        if (books.length > discs.length)  {
//            minSize = discs.length;
//            moreBooks = true;
//            equalObjects = false;
//        }  else  if (discs.length > books.length)  {
//            minSize = books.length;
//            moreBooks = false;
//            equalObjects = false;
//        }  else  {
//            minSize = books.length;
//            moreBooks = false;
//            equalObjects = true;
//        }
//        
//        System.out.println("Min Size: " + minSize);
//        
//        // persist books and discs
//        for (int c = 0; c < minSize; c++)  {
//            Book book = books[c];
//            Disc disc = discs[c];
//            
//            System.out.println(book);
//            System.out.println(disc);
//            
//            persist(book);
//            persist(disc);
//        }
//        
//        if (!equalObjects)  {
//            if (moreBooks)  {
//                for (int c = minSize; c < books.length; c++)  {
//                    Book book = books[c];
//                    persist(book);
//                }
//            }  else  {
//                for (int c = minSize; c < discs.length; c++)  {
//                    Disc disc = discs[c];
//                    persist(disc);
//                }
//            }
//        }
//        
//        //tx.commit();
//    }
    
    
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
        
        String tablename = Medium.getTableName(); // Medium
        String key = Medium.getPrimaryBookID();
        
        String queryString = String.format("from %s where %s is not null", tablename, key);
        Query<Book> query = entityManager.createQuery(queryString);
        List<Book> books = query.list();
        tx.commit();
        
        Book[] media = new Book[books.size()];
        media = books.toArray(media);
        return media;
    }

    @Override
    public Disc[] getDiscs() {
        Session entityManager = sessionFactory.getCurrentSession();
        Transaction tx = entityManager.beginTransaction();
        
        String tablename = Medium.getTableName();
        String key = Medium.getPrimaryDiscID();
        
        String queryString = String.format("from %s where %s is not null", tablename, key);
        Query<Disc> query = entityManager.createQuery(queryString);
        List<Disc> discs = query.list();
        tx.commit();
        
        Disc[] media = new Disc[discs.size()];
        media = discs.toArray(media);
        return media;
    }

    @Override
    public Medium findBook(String isbn) {
        Session entityManager = sessionFactory.getCurrentSession();
        Transaction tx = entityManager.beginTransaction();
    
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = builder.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);
        query.where(builder.equal(root.get(Medium.getPrimaryBookID()), isbn));
        Query<Book> q = entityManager.createQuery(query);
        List<Book> books = q.getResultList();
        Book book = null;
        if (books.size() > 0)  {
            book = books.get(0);
        }
        tx.commit();
        
        return book;
    }

    @Override
    public Medium findDisc(String barcode) {
        Session entityManager = sessionFactory.getCurrentSession();
        Transaction tx = entityManager.beginTransaction();
        
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Disc> query = builder.createQuery(Disc.class);
        Root<Disc> root = query.from(Disc.class);
        query.where(builder.equal(root.get(Medium.getPrimaryDiscID()), barcode));
        Query<Disc> q = entityManager.createQuery(query);
        List<Disc> discs = q.getResultList();
        Disc disc = null;
        if (discs.size() > 0)  {
            disc = discs.get(0);
        }
        tx.commit();
        
        return disc;
    }
}
