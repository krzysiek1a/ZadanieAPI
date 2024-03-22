package pl.orange.app.resources;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReporter {
    static ExtentReports extent;
    private static final String path = System.getProperty("user.dir") + "\\reports\\";
    public static ExtentReports getReportObject() {
        ExtentSparkReporter reporter = new ExtentSparkReporter(path + "\\ExtentReport.html");
        reporter.config().setReportName("Results");
        reporter.config().setDocumentTitle("Test Results");
        reporter.config().setTheme(Theme.STANDARD);
        extent = new ExtentReports();
        extent.attachReporter(reporter);
        return extent;
    }
}
