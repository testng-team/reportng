package org.uncommons.reportng.sample;

import java.util.Arrays;
import java.util.Iterator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * An example of using a TestNG DataProviderTest. This used in the sample report to verify that
 * ReportNG can deal with this scenario correctly.
 */
public class DataProviderTest {

    @DataProvider(name = "arrayProvider")
    public Object[][] dataArray() {
        return new Object[][]{{"One", 1.0d}, {"Two", 2.0d}, {"Three", 3.0d}};
    }


    @DataProvider(name = "iteratorProvider")
    public Iterator<Object[]> dataIterator() {
        return Arrays.asList(new Object[]{"One", 1.0d},
            new Object[]{"Two", 2.0d},
            new Object[]{"Three", 3.0d}).iterator();
    }


    @Test(groups = "should-pass", dataProvider = "arrayProvider")
    public void testProvider(String data1, double data2) {
        // Do nothing.
    }


    @Test(groups = "should-pass", dataProvider = "iteratorProvider")
    public void testProvider2(String data1, double data2) {
        // Do nothing.
    }
}
