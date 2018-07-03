package org.uncommons.reportng.sample;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * A configuration failure for testing that the report correctly reports configuration failures.
 */
public class FailedConfiguration {

    /**
     * A configuration method that will fail causing any test cases in this class to be skipped.
     */
    @BeforeClass
    public void configure() {
        throw new RuntimeException("Configuration failed.");
    }

    /**
     * This test ought to be skipped since the configuration for this class will fail.
     */
    @Test
    public void thisShouldBeSkipped() {
        assert false : "This method is supposed to be skipped.";
    }
}
