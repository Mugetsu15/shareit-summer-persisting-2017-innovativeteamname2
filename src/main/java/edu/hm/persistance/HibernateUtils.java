package edu.hm.persistance;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility Class for Hibernate Database.
 * @author Daniel
 *
 */

// CHECKSTYLE:OFF // Konstruktoren von Hilfsklassen sollten nicht public oder default deklariert sein.
public class HibernateUtils {
 // CHECKSTYLE:ON
    private static SessionFactory sessionFactory;

    static {
       sessionFactory = new Configuration().configure().buildSessionFactory();
    }
    
    /**
     * Getter for the session factory.
     * @return current session factory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    /**
     * Shutdown method for the Hibernate Database.
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}