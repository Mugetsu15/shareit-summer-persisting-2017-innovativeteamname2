package edu.hm.management.persistence;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.hm.GuiceTestModule;
import edu.hm.management.bib.Fsk;
import edu.hm.management.bib.IMediaService;
import edu.hm.management.bib.MediaResource;
import edu.hm.management.bib.MediaServiceImpl;
import edu.hm.management.media.*;
import edu.hm.management.user.AuthenticationImpl;
import edu.hm.management.user.AuthenticationResource;
import edu.hm.management.user.IAuthentication;
import edu.hm.persistance.IMediaPersistence;
import edu.hm.persistance.MediaPersistenceImpl;

import org.junit.*;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

/**
 * Tests for the Persistence.
 */
public class MediaPersistenceTest {
    
    @Inject
    private IMediaPersistence database;
    
    private IMediaService service = new MediaServiceImpl(database);
    private IAuthentication tokenService = new AuthenticationImpl();
    
    private MediaResource resource = new MediaResource(service);
    private AuthenticationResource tokenResource = new AuthenticationResource();
    
    /**
     * Deleting the List each time.
     * @throws Exception in case of failure
     */
    @Before
    public void setUp() throws Exception {
        Guice.createInjector(new GuiceTestModule()).injectMembers(this);
        
        service = new MediaServiceImpl(database);
        resource = new MediaResource(service, tokenService);
    }
    
    private final Book bk1 = new Book("Author-909-4", "978-1-56619-909-4", "Title-909-4");
    private final Book bk2 = new Book("Author-9462-6", "978-1-4028-9462-6", "Title-9462-6");
    private final Book bk3 = new Book("Richard Castle", "978-3-8642-5007-1", "Heat Wave");
     
    private final Disc ds1 = new Disc("978-1-56619-909-4", "Director-909-4", Fsk.FSK12.getFsk(), "Title-909-4");
    private final Disc ds2 = new Disc("978-1-4028-9462-6", "Director-9462-6", Fsk.FSK18.getFsk(), "Title-9462-6");
    //private final Disc ds3 = new Disc("8-88837-34272-8", "James Arthur", Fsk.FSK0.getFsk(), "Impossible");

    /**
     * Tests on persistBook.
     */
    @Test
    public void testPersistBook() {
        database.persistBook(bk1);
        assertEquals(1, database.getBooks().length);
    }
    
    /**
     * Tests on persistDisc.
     */
    @Test
    public void testPersistDisc() {
        database.persistDisc(ds1);
        assertEquals(1, database.getDiscs().length);
    }
    
    /**
     * Tests on getBooks.
     */
    @Test
    public void testGetBooks()  {
        // Can be empty
        Book[] books = database.getBooks();
        assertEquals(0, books.length);
        
        database.persistBook(bk1);
        database.persistBook(bk2);
        books = database.getBooks();
        assertEquals(2, books.length);
    }
    
    /**
     * Tests on getDiscs.
     */
    @Test
    public void testGetDiscs()  {
        // Can be empty
        Disc[] discs = database.getDiscs();
        assertEquals(0, discs.length);
        
        database.persistDisc(ds1);
        database.persistDisc(ds2);
        discs = database.getDiscs();
        assertEquals(2, discs.length);
    }
    
    /**
     * Tests on findBook.
     */
    @Test
    public void testFindBook()  {
        database.persistBook(bk3);
        
        Book shouldBeBk3 = (Book) database.findBook(bk3.getIsbn());
        assertEquals(bk3, shouldBeBk3);
        
        // May not exists
        Book shouldNotExist = (Book) database.findBook(bk2.getIsbn());
        assertEquals(null, shouldNotExist);
    }
    
    /**
     * Tests on findDisc.
     */
    @Test
    public void testFindDisc()  {
        database.persistDisc(ds2);
        
        Disc shouldBeDs2 = (Disc) database.findDisc(ds2.getBarcode());
        assertEquals(ds2, shouldBeDs2);
        
        // May not exists
        Disc shouldNotExist = (Disc) database.findDisc(ds1.getBarcode());
        assertEquals(null, shouldNotExist);
    }
}
