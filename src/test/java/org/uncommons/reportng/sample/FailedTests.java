package org.uncommons.reportng.sample;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

/**
 * Some test failures to be included in the sample output.
 */
@Test(groups = "should-fail")
public class FailedTests {

    @Test
    public void assertionFailure() {
        assert false : "This test failed.";
    }


    @Test
    public void assertionFailureWithMultilineMessage() {
        Assert.fail("This test failed.\nIts message is on multiple lines.\n     The last one has leading whitespace.");
    }


    @Test
    public void assertionFailureWithOutput() {
        Reporter.log("Here is some output from an unsuccessful test.");
        Assert.fail("This test failed.");
    }


    @Test
    public void exceptionThrown() {
        throw new IllegalStateException("Test failed.",
            new UnsupportedOperationException()); // Nested cause.
    }
}
