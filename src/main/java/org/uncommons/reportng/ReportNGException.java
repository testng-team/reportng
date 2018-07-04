package org.uncommons.reportng;

/**
 * Unchecked exception thrown when an unrecoverable error occurs during report generation.
 */
public class ReportNGException extends RuntimeException {

    public ReportNGException(String string, Throwable throwable) {
        super(string, throwable);
    }
}
