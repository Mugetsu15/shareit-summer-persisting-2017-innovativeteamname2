package edu.hm.management.media;

import java.io.Serializable;

/**
 * Class represents a Copy of a Medium which has a Medium Object and an owner.
 * @author Daniel Gabl
 *
 */
//@Entity
public class Copy implements Serializable  {
    
    /**
     * UID.
     */
    private static final long serialVersionUID = 6843857203681986574L;

    /**
     * ID of Copy for Persistence.
     */
    //@Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    //@Column(name="COPY_ID")
    private long copyID;
    
    /**
     * Medium which was copied.
     */
    private final Medium medium;
    
    /**
     * User who lend this copy.
     */
    private final String owner;
    
    /**
     * Constructor for a Copy Object.
     * @param owner Owner of the Copy.
     * @param medium Medium which was lend.
     */
    public Copy(String owner, Medium medium)  {
        this.owner = owner;
        this.medium = medium;
    }
    
    /**
     * Getter for the Medium.
     * @return medium.
     */
    public Medium getMedium()  {
        return medium;
    }
    
    /**
     * Getter for the Username.
     * @return username.
     */
    public String getUsername()  {
        return owner;
    }

}
