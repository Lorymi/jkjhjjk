package org.acra.collector;

import java.util.EnumMap;
import org.acra.ReportField;
import org.acra.util.JSONReportBuilder;
import org.json.JSONObject;

public final class CrashReportData extends EnumMap {
    private static final long serialVersionUID = 4112578634029874840L;

    public CrashReportData() {
        super(ReportField.class);
    }

    public String getProperty(ReportField reportField) {
        return (String) super.get(reportField);
    }

    public JSONObject toJSON() {
        return JSONReportBuilder.buildJSONReport(this);
    }
}
