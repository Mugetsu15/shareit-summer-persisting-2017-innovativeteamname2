package edu.hm.test.management.persistence;

import com.google.inject.AbstractModule;

import edu.hm.management.bib.IMediaService;
import edu.hm.management.bib.MediaServiceImpl;
import edu.hm.persistance.IMediaPersistence;
import edu.hm.persistance.MediaPersistenceImpl;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by axel on 26.05.17.
 */
public class GuiceTestModule extends AbstractModule {
    
    /**
     * Configuration for Test Module.
     */
    protected void configure() {
        bind(IMediaService.class).to(MediaServiceImpl.class);
        bind(IMediaPersistence.class).to(MediaPersistenceImpl.class);
        bind(SessionFactory.class).toInstance(new Configuration().configure().buildSessionFactory());
    }

}
