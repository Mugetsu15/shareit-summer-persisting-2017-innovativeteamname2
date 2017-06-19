package edu.hm.management.bib;

import static org.junit.Assert.*;

import org.junit.Test;


public class MediaServiceImplTest {
    
    MediaServiceImpl service = new MediaServiceImpl(null);

    @Test
    public void testIsbnChecker() {
        assertTrue("Correctness-check for correrct ISBN failed", service.checkISBN13("978-3864250071"));
    }
    

    
}
