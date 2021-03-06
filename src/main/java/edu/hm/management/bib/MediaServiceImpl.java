package edu.hm.management.bib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import edu.hm.management.media.Book;
import edu.hm.management.media.Disc;
import edu.hm.management.media.Medium;
import edu.hm.persistance.IMediaPersistence;

/**
 * Implementation for Media Service Interface.
 * @author Daniel Gabl
 *
 */
public class MediaServiceImpl implements IMediaService {
    
    /**
     * Interface of Persistence.
     */
    private IMediaPersistence database;
    
    /**
     * Boolean to check if database was initialized.
     */
    private static boolean initialized = false;
    
    
    /**
     * Default Constructor.
     * @param persistenceInterface Injected MediaPersistence-Interface
     */
    @Inject
    public MediaServiceImpl(IMediaPersistence persistenceInterface)  {
        database = persistenceInterface;
        
//        if (!initialized)  {
//            database.initialize();
//            initialized = true;
//        }
    }
    
    /**
     * Checks if a given ISBN number is valid or not.
     * @param isbn ISBN number to check
     * @return true if valid, else false
     */
    boolean checkISBN13(String isbn)  {
        final int isbnLength = 13 - 1; // Hello Checkstyle. :)
        final int moduloFactor = 10;
        final int evenMultiplicator = 3;
        
        boolean flag = false;
        
        isbn = cleanISBN(isbn);
        
        int sum = 0;
        try  {
            for (int c = 0; c < isbnLength; c++)  {
                int digit = Integer.parseInt(isbn.substring(c, c + 1));
                // Zur Berechnung der Pr�fziffer bei der ISBN-13 werden alle zw�lf Ziffern der noch unvollständigen ISBN addiert,
                // wobei die Ziffern mit gerader Position (also die zweite, vierte und so weiter) dreifachen Wert erhalten.
                int mult = 1;
                if ((c + 1) % 2 == 0)  {
                    mult = evenMultiplicator;
                }
                sum += digit * mult;
            }
            
            // Checksumme = (Die Zehnerpotenz, die gr��er als die Summe ist) - Summe
            int checksum = (sum / moduloFactor + 1) * moduloFactor - sum;
            
            //Ist das Endergebnis 10, ist die Pr�fziffer 0.
            if (checksum == moduloFactor)  {
                checksum = 0;
            }
            
            if (checksum == Integer.parseInt(isbn.substring(isbnLength)))  {
                flag = true;
            }
        } catch(NumberFormatException | StringIndexOutOfBoundsException e)  {
            flag = false;
        }
        
        // https://de.wikipedia.org/wiki/Internationale_Standardbuchnummer#ISBN-13
        // String isbn = "978-3-12-732320-7";

        return flag;
    }
    
    /**
     * Removes all spaces and minus from the given String.
     * @param isbn ISBN number to clean
     * @return cleaned ISBN
     */
    private String cleanISBN(String isbn)  {
        isbn = isbn.replace("-", "");
        isbn = isbn.replace(" ", "");
        
        return isbn;
    }

    @Override
    public MediaServiceResult addBook(Book book) {
        book = new Book(book.getAuthor(), cleanISBN(book.getIsbn()), book.getTitle());
        if (!book.getAuthor().isEmpty() && !book.getIsbn().isEmpty() && !book.getTitle().isEmpty()) {
            List<Book> books = new ArrayList<>(Arrays.asList(database.getBooks()));
            if (!books.contains(book))  {
                boolean isbnExist = false;
                for (Book bk : books)  {
                    if (cleanISBN(bk.getIsbn()).equals(cleanISBN(book.getIsbn())))  {
                        isbnExist = true;
                        break;
                    }
                }
                if (!isbnExist)  {
                    if (checkISBN13(book.getIsbn()))  {
                        database.persistBook(book);
                        return MediaServiceResult.OKAY;
                    }  else  {
                        return MediaServiceResult.ISBNBROKEN;
                    }
                }  else  {
                    return MediaServiceResult.DUPLICATEISBN;
                }
            }
            return MediaServiceResult.DUPLICATEOBJ;
        }
        return MediaServiceResult.BADREQUEST;
    }
    
    @Override
    public MediaServiceResult addDisc(Disc disc) {
        disc = new Disc(cleanISBN(disc.getBarcode()), disc.getDirector(), disc.getFsk(), disc.getTitle());
        if (!disc.getDirector().isEmpty() && !disc.getBarcode().isEmpty() && !disc.getTitle().isEmpty()) {
            List<Disc> discs = new ArrayList<>(Arrays.asList(database.getDiscs()));
            if (!discs.contains(disc))  {
                boolean barcodeExists = false;
                for (Disc ds : discs)  {
                    if (cleanISBN(ds.getBarcode()).equals(cleanISBN(disc.getBarcode())))  {
                        barcodeExists = true;
                        break;
                    }
                }
                if (!barcodeExists)  {
                    if (checkISBN13(disc.getBarcode()))  {
                        database.persistDisc(disc);
                        return MediaServiceResult.OKAY;
                    }  else  {
                        return MediaServiceResult.ISBNBROKEN;
                    }
                }  else  {
                    return MediaServiceResult.DUPLICATEISBN;
                }
            }
            return MediaServiceResult.DUPLICATEOBJ;
        }
        return MediaServiceResult.BADREQUEST;
    }

    @Override
    public Medium[] getBooks() {
        return database.getBooks();
    }

    @Override
    public Medium[] getDiscs() {
        return database.getDiscs();
    }

    @Override
    public MediaServiceResult updateBook(Book book)  {
        book = new Book(book.getAuthor(), cleanISBN(book.getIsbn()), book.getTitle());
        boolean isbnInList = false;
        List<Book> books = new ArrayList<>(Arrays.asList(database.getBooks()));
        for (Book bk : books)  {
            if (cleanISBN(bk.getIsbn()).equals(cleanISBN(book.getIsbn())))  {
                isbnInList = true;
            }
        }
        if (isbnInList)  {
            for (int c = 0; c < books.size(); c++)  {
                Book bk = books.get(c);
                if (cleanISBN(bk.getIsbn()).equals(cleanISBN(book.getIsbn())))  {
                    String title = bk.getTitle();
                    String author = bk.getAuthor();
                    
                    if (!bk.getAuthor().equals(book.getAuthor()) && !book.getAuthor().isEmpty())  {
                        author = book.getAuthor();
                    }
                    if (!bk.getTitle().equals(book.getTitle()) && !book.getTitle().isEmpty())  {
                        title = book.getTitle();
                    }
                    
                    MediaServiceResult result = MediaServiceResult.BADREQUEST;
                    
                    if (!title.equals(bk.getTitle()) || !author.equals(bk.getAuthor()))  {  // Data was modified
                        Book remove = books.remove(c);
                        database.deleteBook(remove);
                        Book newbook = new Book(author, book.getIsbn(), title);
                        result = addBook(newbook);
                    }
                    
                    return result;
                }
            }
        }
        return MediaServiceResult.ISBNNOTFOUND;
    }
    //CHECKSTYLE:OFF (Zyklomatische Komplexität beträgt 15 (Obergrenze ist 13).)
    @Override
    //CHECKSTYLE:ON
    public MediaServiceResult updateDisc(Disc disc) {
        disc = new Disc(cleanISBN(disc.getBarcode()), disc.getDirector(), disc.getFsk(), disc.getTitle());
        boolean barcodeInList = false;
        List<Disc> discs = new ArrayList<>(Arrays.asList(database.getDiscs()));
        for (Disc ds : discs)  {
            if (cleanISBN(ds.getBarcode()).equals(cleanISBN(disc.getBarcode())))  {
                barcodeInList = true;
            }
        }
        if (barcodeInList)  {
            for (int c = 0; c < discs.size(); c++)  {
                Disc ds = discs.get(c);
                if (cleanISBN(ds.getBarcode()).equals(cleanISBN(disc.getBarcode())))  {
                    String director = ds.getDirector();
                    int fsk = ds.getFsk();
                    String title = ds.getTitle();
                                       
                    if (!ds.getDirector().equals(disc.getDirector()) && !disc.getDirector().isEmpty())  {
                        director = disc.getDirector();
                    }
                    if (ds.getFsk() != disc.getFsk() && disc.getFsk() >= 0)  {
                        fsk = disc.getFsk();
                    }
                    if (!ds.getTitle().equals(disc.getTitle()) && !disc.getTitle().isEmpty())  {
                        title = disc.getTitle();
                    }
                    
                    MediaServiceResult result = MediaServiceResult.BADREQUEST;
                    
                    if (!director.equals(ds.getDirector()) || fsk != ds.getFsk() || !title.equals(ds.getTitle()))  {  // Data was modified
                        Disc remove = discs.remove(c);
                        database.deleteDisc(remove);
                        Disc newdisc = new Disc(disc.getBarcode(), director, fsk, title);
                        result = addDisc(newdisc);
                    }
                    return result;
                }
            }
        }
        return MediaServiceResult.ISBNNOTFOUND;
    }

    @Override
    public Medium findBook(String isbn) {
        isbn = cleanISBN(isbn);
        return database.findBook(isbn);
    }

    @Override
    public Medium findDisc(String barcode) {
        barcode = cleanISBN(barcode);
        return database.findDisc(barcode);
    }

}
