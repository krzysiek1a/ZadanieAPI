package pl.orange.app.testComponents;

import com.aventstack.extentreports.Status;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseTest {

    protected static RequestSpecification reqSpec;

    @BeforeClass
    public void setUp() {
        reqSpec = new RequestSpecBuilder()
                .setBaseUri("app.orange.pl")
                .setContentType(ContentType.JSON)
                .build();

        RequestLoggingFilter reqLog = new RequestLoggingFilter();
        ResponseLoggingFilter resLog = new ResponseLoggingFilter();
        RestAssured.filters(reqLog, resLog);
    }

    public void reportLog(String message) {
        Listeners.extentTest.get().log(Status.INFO, message);
    }

}
