package edu.hm.management.media;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;

import edu.hm.GuiceTestModule;
import edu.hm.management.bib.Fsk;
import edu.hm.management.bib.IMediaService;
import edu.hm.management.bib.MediaResource;
import edu.hm.management.bib.MediaServiceImpl;
import edu.hm.management.bib.MediaServiceResult;
import edu.hm.management.media.Book;
import edu.hm.management.media.Disc;
import edu.hm.management.user.AuthenticationResource;
import edu.hm.management.user.IAuthentication;
import edu.hm.management.user.Role;
import edu.hm.management.user.User;
import edu.hm.persistance.IMediaPersistence;
import edu.hm.management.user.AuthenticationImpl;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

/**
 * Tests on MediaResource Class.
 * @author Daniel Gabl
 *
 */
public class MediaResourceTest {
        
    @Inject
    private MediaResource resource; // = new MediaResource(service);
    
    @Inject
    private IMediaPersistence database;
    
    @Inject
    private IMediaService service; // = new MediaServiceImpl(database);
    
    private IAuthentication tokenService = new AuthenticationImpl();
    private AuthenticationResource tokenResource = new AuthenticationResource();
    
    private final static String TOKEN = "rootToken";
        
    private final Book bk1 = new Book("Richard Castle", "978-3864250101", "Frozen Heat");
    private final String isbn = "978-3864250101";

    private final Disc ds1 = new Disc("978-3864250101", "Director-Frozen", Fsk.FSK16.getFsk(), "Title-Frozen");
    private final String barcode = "978-3864250101";
    
    private final User rootUser = new User("rootUsr", "rootpw", Role.ROOT);
    
    /**
     * Deleting the List each time.
     * @throws Exception in case of failure
     */
    @Before
    public void setUp() throws Exception {
        Guice.createInjector(new GuiceTestModule()).injectMembers(this);
        
        tokenService.clearLibary();
        tokenService = new AuthenticationImpl();
        tokenResource = new AuthenticationResource(tokenService);
        
        //service.clearLibary();
        service = new MediaServiceImpl(database);
        resource = new MediaResource(service, tokenService);
    }
    
    /**
     * Test on createBook.
     */
    @Test
    public void testCreateBook() {
        // First wrong
        Response rep = resource.createBook(bk1, TOKEN, null);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"code\":" + MediaServiceResult.TOKENNOTVALID.getCode() + ",\"detail\":\""
                + MediaServiceResult.TOKENNOTVALID.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
        
        // Then right
        tokenService.addUser(rootUser);
        tokenService.generateToken(rootUser, null);
        
        rep = resource.createBook(bk1, TOKEN, null);
        repEntity = rep.getEntity().toString();
        expected = "{\"code\":" + MediaServiceResult.OKAY.getCode() + ",\"detail\":\""
                + MediaServiceResult.OKAY.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
    }
    
    /**
     * Test on getBooks.
     */
    @Test
    public void testGetBooks()  {
        // Login
        tokenService.addUser(rootUser);
        tokenService.generateToken(rootUser, null);
        
        Response rep = resource.createBook(bk1, TOKEN, null);
        rep = resource.getBooks(TOKEN);
        String repEntity = rep.getEntity().toString();
        String expected = "[{\"title\":\"Frozen Heat\",\"author\":\"Richard Castle\",\"isbn\":\"9783864250101\"}]";
        
        Assert.assertEquals(expected, repEntity);
    }
    
    /**
     * Test on updateBook.
     */
    @Test
    public void testUpdateBook() {
        // First wrong
        Book update = new Book("New Author", isbn, "New Title");
        Response rep = resource.updateBook(update, TOKEN, null);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"code\":" + MediaServiceResult.TOKENNOTVALID.getCode() + ",\"detail\":\""
                + MediaServiceResult.TOKENNOTVALID.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
        
        // Then right
        tokenService.addUser(rootUser);
        tokenService.generateToken(rootUser, null);
        
        resource.createBook(bk1, TOKEN, null);
        rep = resource.updateBook(update, TOKEN, null);
        repEntity = rep.getEntity().toString();
        expected = "{\"code\":" + MediaServiceResult.OKAY.getCode() + ",\"detail\":\""
                + MediaServiceResult.OKAY.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
    }
    
    /**
     * Test on findBook.
     */
    @Test
    public void testFindBook() {
        // Login
        tokenService.addUser(rootUser);
        tokenService.generateToken(rootUser, null);
        
        resource.createBook(bk1, TOKEN, null);
        Response rep = resource.findBook(isbn, TOKEN);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"title\":\"Frozen Heat\",\"author\":\"Richard Castle\",\"isbn\":\"9783864250101\"}";
        Assert.assertEquals(expected, repEntity);
    }
    
    
    /**
     * Test on createDisc.
     */
    @Test
    public void testCreateDisc()  {
        // First wrong
        Response rep = resource.createDisc(ds1, TOKEN, null);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"code\":" + MediaServiceResult.TOKENNOTVALID.getCode() + ",\"detail\":\""
                + MediaServiceResult.TOKENNOTVALID.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
        
        // Then right
        tokenService.addUser(rootUser);
        tokenService.generateToken(rootUser, null);
        
        rep = resource.createDisc(ds1, TOKEN, null);
        repEntity = rep.getEntity().toString();
        expected = "{\"code\":" + MediaServiceResult.OKAY.getCode() + ",\"detail\":\""
                + MediaServiceResult.OKAY.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
    }
    
    /**
     * Test on getDiscs.
     */
    @Test
    public void testGetDiscs()  {
        // Login
        tokenService.addUser(rootUser);
        tokenService.generateToken(rootUser, null);
        
        resource.createDisc(ds1, TOKEN, null);
        Response rep = resource.getDiscs(TOKEN);
        String repEntity = rep.getEntity().toString();
        String expected = "[{\"title\":\"Title-Frozen\",\"barcode\":\"9783864250101\",\"director\":\"Director-Frozen\",\"fsk\":16}]";
        
        Assert.assertEquals(expected, repEntity);
    }
    
    /**
     * Test on updateDisc.
     */
    @Test
    public void testUpdateDisc() {
        // First wrong
        Disc update = new Disc(barcode, "New Director", Fsk.FSK0.getFsk(), "New Title");
        Response rep = resource.updateDisc(update, TOKEN, null);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"code\":" + MediaServiceResult.TOKENNOTVALID.getCode() + ",\"detail\":\""
                + MediaServiceResult.TOKENNOTVALID.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
        
        // Then right
        tokenService.addUser(rootUser);
        tokenService.generateToken(rootUser, null);
        
        resource.createDisc(ds1, TOKEN, null);
        rep = resource.updateDisc(update, TOKEN, null);
        repEntity = rep.getEntity().toString();
        expected = "{\"code\":" + MediaServiceResult.OKAY.getCode() + ",\"detail\":\""
                + MediaServiceResult.OKAY.getNote() +  "\"}";
        Assert.assertEquals(expected, repEntity);
    }
    
    /**
     * Test on findBook.
     */
    @Test
    public void testFindDisc() {
        // Login
        tokenService.addUser(rootUser);
        tokenService.generateToken(rootUser, null);
        
        resource.createDisc(ds1, TOKEN, null);
        Response rep = resource.findDisc(barcode, TOKEN);
        String repEntity = rep.getEntity().toString();
        String expected = "{\"title\":\"Title-Frozen\",\"barcode\":\"9783864250101\",\"director\":\"Director-Frozen\",\"fsk\":16}";
        Assert.assertEquals(expected, repEntity);
    }
}
