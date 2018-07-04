package org.uncommons.reportng.sample;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * These tests are skipped because they depend on failed tests in another class.
 */
@Test(groups = "should-skip")
public class SkippedTests {

    @Test(dependsOnGroups = "should-fail")
    public void skippedDueToDependentGroup() {
        Assert.fail("This method is supposed to be skipped.");
    }


    @Test(dependsOnMethods = "skippedDueToDependentGroup")
    public void skippedDueToDependentMethod() {
        Assert.fail("This method is supposed to be skipped.");
    }
}
