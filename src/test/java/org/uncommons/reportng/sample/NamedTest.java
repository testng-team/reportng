package org.uncommons.reportng.sample;

import org.testng.ITest;
import org.testng.annotations.Test;

/**
 * Test for named tests.
 */
public class NamedTest implements ITest {

    public String getTestName() {
        return "NamedTest";
    }


    @Test
    public void testNamed() {
        // Do nothing, will pass.
    }
}
