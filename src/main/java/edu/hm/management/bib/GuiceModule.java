package edu.hm.management.bib;

import com.google.inject.AbstractModule;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Google Guice Module Class.
 */
public class GuiceModule extends AbstractModule {
    
    /**
     * configure-Function.
     */
    protected void configure() {
        bind(SessionFactory.class).toInstance(new Configuration().configure().buildSessionFactory());
    }

}
