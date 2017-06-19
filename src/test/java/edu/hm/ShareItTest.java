package edu.hm;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.hm.management.media.MediaResourceTest;
import edu.hm.management.media.MediaServiceTest;
import edu.hm.management.persistence.MediaPersistenceTest;
import edu.hm.management.user.AuthenticationResourceTest;
import edu.hm.management.user.AuthenticationServiceTest;
import edu.hm.management.user.UserTest;

/**
 * Testing all tests at the same time.
 * @author Daniel Gabl
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ MediaServiceTest.class, MediaResourceTest.class, AuthenticationResourceTest.class,
    AuthenticationServiceTest.class, UserTest.class, MediaPersistenceTest.class})
public class ShareItTest {

}