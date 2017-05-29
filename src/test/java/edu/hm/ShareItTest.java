package edu.hm;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.hm.test.management.media.MediaResourceTest;
import edu.hm.test.management.media.MediaServiceTest;
import edu.hm.test.management.user.AuthenticationResourceTest;
import edu.hm.test.management.user.AuthenticationServiceTest;
import edu.hm.test.management.user.UserTest;

/**
 * Testing all tests at the same time.
 * @author Daniel Gabl
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ MediaServiceTest.class, MediaResourceTest.class, AuthenticationResourceTest.class, AuthenticationServiceTest.class, UserTest.class})
public class ShareItTest {

}