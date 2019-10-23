package org.acra.sender;

public class ReportSenderException extends Exception {
    public ReportSenderException(String str) {
        super(str);
    }

    public ReportSenderException(String str, Throwable th) {
        super(str, th);
    }
}
