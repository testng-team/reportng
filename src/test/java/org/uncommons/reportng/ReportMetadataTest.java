package org.uncommons.reportng;

import java.util.Locale;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for the {@link ReportMetadata} class.
 */
public class ReportMetadataTest {

    @Test
    public void testDefaultLocale() {
        // Unset any previously set property.
        System.getProperties().remove(ReportMetadata.LOCALE_KEY);
        // Make sure we know what the default locale is before we start.
        Locale.setDefault(new Locale("en", "GB"));

        ReportMetadata metadata = new ReportMetadata();
        String locale = metadata.getLocale().toString();
        Assert.assertEquals(locale, "en_GB", "Wrong locale: " + locale);
    }


    @Test
    public void testLocaleLanguageOnly() {
        // Unset any previously set property.
        System.setProperty(ReportMetadata.LOCALE_KEY, "fr");

        ReportMetadata metadata = new ReportMetadata();
        String locale = metadata.getLocale().toString();
        Assert.assertEquals(locale, "fr", "Wrong locale: " + locale);
    }


    @Test
    public void testLocaleLanguageAndCountry() {
        // Unset any previously set property.
        System.setProperty(ReportMetadata.LOCALE_KEY, "fr_CA");

        ReportMetadata metadata = new ReportMetadata();
        String locale = metadata.getLocale().toString();
        Assert.assertEquals(locale, "fr_CA", "Wrong locale: " + locale);
    }


    @Test
    public void testLocaleLanguageCountryAndVariant() {
        // Unset any previously set property.
        System.setProperty(ReportMetadata.LOCALE_KEY, "fr_CA_POSIX");

        ReportMetadata metadata = new ReportMetadata();
        String locale = metadata.getLocale().toString();
        Assert.assertEquals(locale, "fr_CA_POSIX", "Wrong locale: " + locale);
    }
}
