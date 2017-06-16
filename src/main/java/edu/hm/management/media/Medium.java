package edu.hm.management.media;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Class represents a Medium Object which has a title.
 * @author Daniel Gabl
 *
 */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Medium implements Serializable  {
    
    /**
     * Name of DataBase table.
     */
    private static final String DBTABLENAME = "Medium";
    
    /**
     * Name of primary ID of Book class.
     */
    private static final String PRIMARYBOOKID = "isbn";
    
    /**
     * Name pf primary ID of Disc class.
     */
    private static final String PRIMARYDISCID = "barcode";
    
    /**
     * Getter for the name of the database table.
     * @return name of the database table
     */
    public static String getTableName()  {
        return DBTABLENAME;
    }
    
    /**
     * Getter for the name of the primary id of the Book Class.
     * @return name of the primary id of the Book Class.
     */
    public static String getPrimaryBookID()  {
        return PRIMARYBOOKID;
    }
    
    /**
     * Getter for the name of the primary id of the Disc Class.
     * @return name of the primary id of the Disc Class.
     */
    public static String getPrimaryDiscID()  {
        return PRIMARYDISCID;
    }
    
    
    
    /**
     * UID.
     */
    private static final long serialVersionUID = 2299215975687951843L;
    
    /**
     * ID of Medium.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;
    
    
    /**
     * Title of this Medium.
     */
    private final String title;
    
    /**
     * Default Constructor for Hibernate.
     */
    Medium()  {
        title = null;
        
    }
    /**
     * Constructor for a Medium Object.
     * @param title Title of Medium
     */
    public Medium(String title)  {
        this.title = title;
    }
    
    /**
     * Getter for the Title of this Medium.
     * @return title of medium.
     */
    public String getTitle()  {
        return title;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }
    
    @Override
    public String toString()  {
        return String.format("The Title of this Medium is '%s'", title);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)  {
            return true;
        }
        if (obj == null)  {
            return false;
        }
        if (getClass() != obj.getClass())  {
            return false;
        }
        Medium other = (Medium) obj;
        if (title == null) {
            if (other.title != null)  {
                return false;
            }
        } else if (!title.equals(other.title))  {
            return false;
        }
        return true;
    }

}
