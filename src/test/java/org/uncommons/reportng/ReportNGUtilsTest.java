package org.uncommons.reportng;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for {@link ReportNGUtils}.
 */
public class ReportNGUtilsTest {

    private final ReportNGUtils utils = new ReportNGUtils();

    @Test
    public void testEscapeTags() {
        final String originalString = "</ns1:ErrorCode>";
        String escapedString = utils.escapeString(originalString);
        Assert.assertEquals(escapedString, "&lt;/ns1:ErrorCode&gt;",
            "Wrong escaping: " + escapedString);
    }


    @Test
    public void testEscapeQuotes() {
        final String originalString = "\"Hello\'";
        String escapedString = utils.escapeString(originalString);
        Assert.assertEquals(escapedString, "&quot;Hello&apos;",
            "Wrong escaping: " + escapedString);
    }


    @Test
    public void testEscapeAmpersands() {
        final String originalString = "&&";
        String escapedString = utils.escapeString(originalString);
        Assert.assertEquals(escapedString, "&amp;&amp;",
            "Wrong escaping: " + escapedString);
    }


    @Test
    public void testEscapeSpaces() {
        final String originalString = "    ";
        // Spaces should not be escaped in XML...
        String escapedString = utils.escapeString(originalString);
        Assert.assertEquals(escapedString, originalString,
            "Wrong escaping: " + escapedString);

        // ...only in HTML.
        escapedString = utils.escapeHTMLString(originalString);
        Assert.assertEquals(escapedString, "&nbsp;&nbsp;&nbsp; ",
            "Wrong escaping: " + escapedString);

    }

    @Test
    public void testFormatIntegerPercentage() {
        String percentage = utils.formatPercentage(10, 100);
        Assert.assertEquals(percentage, "10.00%",
            "Wrongly formatted percentage: " + percentage);
    }


    @Test
    public void testFormatFractionalPercentage() {
        String percentage = utils.formatPercentage(2, 3);
        Assert.assertEquals(percentage, "66.67%",
            "Wrongly formatted percentage: " + percentage);
    }
}
