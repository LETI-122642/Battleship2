package iscteiul.ista;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit test for simple App.
 */
class AppTest {

    @Test
    void testApp() {
        assertTrue(true, "Basic test should pass");
    }

    @Test
    void testAppHasMain() {
        // This is a simple test to verify the test framework is working
        App app = new App();
        assertNotNull(app);
    }
}