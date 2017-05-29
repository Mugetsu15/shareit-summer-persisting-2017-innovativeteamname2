package edu.hm;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import edu.hm.management.bib.IMediaService;
import edu.hm.management.bib.MediaServiceImpl;

/**
* Context Listener to enable usage of google guice together with jersey.
*/
public class ShareitServletContextListener extends GuiceServletContextListener {
    
    private static final Injector injector = Guice.createInjector(new ServletModule() {
        @Override
        protected void configureServlets() {
            bind(IMediaService.class).to(MediaServiceImpl.class);
        }
    });

    @Override
    protected Injector getInjector() {
        return injector;
    }
    /**
     * This method is only required for the HK2-Guice-Bridge in the Application class.
     * @return Injector instance.
     */
    static Injector getInjectorInstance() {
        return injector;
    }
}