package pl.orange.app;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pl.orange.app.model.LastTestDeviceReq;
import pl.orange.app.model.ReportPotentialErrorReq;
import pl.orange.app.model.TestDeviceReq;
import pl.orange.app.testComponents.BaseTest;

import static io.restassured.RestAssured.given;

public class AppTest extends BaseTest {
    private static final String DEVICE_SN = System.getProperty("DEVICE_SN") != null
            ? System.getProperty("DEVICE_SN")
            : "AB12CD345678"; // numer seryjny urządzenia
    private static final String STATION_NUMBER = System.getProperty("STATION_NUMBER") != null
            ? System.getProperty("STATION_NUMBER")
            : "TS-1"; // numer stanowiska
    private static final int MAX_ATTEMPTS = System.getProperty("MAX_ATTEMPTS") != null
            ? Integer.parseInt(System.getProperty("MAX_ATTEMPTS"))
            : 3; // max ilość testów jeśli status to INCONCLUSIVE

    TestDeviceReq testDeviceReq = new TestDeviceReq(DEVICE_SN, STATION_NUMBER);
    LastTestDeviceReq lastTestDeviceReq = new LastTestDeviceReq(DEVICE_SN);
    ReportPotentialErrorReq reportPotentialErrorReq;

    @Test()
    public void testDevice() {
        String testDevicePath = "testDevice";
        String getLastTestResultPath = "getLastTestResult";
        String reportPotentialErrorPath = "reportPotentialError";

        int attempt = 0;
        String status = "";
        String lastStatus = "";
        String date = "";

        reportLog("Wykonaj test urządzenia: " + STATION_NUMBER);

        do {
            // Wykonaj test urządzenia
            reportLog("Sprawdź status testu");

            Response testResponse = given().spec(reqSpec)
                    .basePath(testDevicePath)
                    .body(testDeviceReq)
                    .post();

            Assert.assertEquals(testResponse.getStatusCode(), 200);

            status = testResponse.jsonPath().getString("Status");
            reportLog("Status testu: " + status);
            attempt++;

            // Sprawdź ostatni status testu
            reportLog("Sprawdź ostatni status testu");

            Response lastTestResponse = given().spec(reqSpec)
                    .basePath(getLastTestResultPath)
                    .body(lastTestDeviceReq)
                    .post();

            Assert.assertEquals(lastTestResponse.getStatusCode(), 200);

            lastStatus = lastTestResponse.jsonPath().getString("Status");
            date = lastTestResponse.jsonPath().getString("Date");

            reportLog("Ostatni status testu: " + lastStatus);

            Assert.assertEquals(status, lastStatus, "Różne statusy testu: ");

            reportLog("Status urządzenia: " + status + ". Ostatni status urządzenia: " + lastStatus);
            reportLog("Ilość testów: " + attempt);

            // Jeżeli status testu jest INCONCLUSIVE, powtórz test
        } while (status.equals("INCONCLUSIVE") && lastStatus.equals("INCONCLUSIVE") && attempt < MAX_ATTEMPTS);

        // Jeżeli status testu to INCONCLUSIVE po 3 próbach, zgłoś potencjalny błąd
        reportPotentialError(status, attempt, date, reportPotentialErrorPath);
    }

    private void reportPotentialError(String status, int attempt, String date, String reportPotentialErrorPath) {
        if (status.equals("INCONCLUSIVE") && attempt == MAX_ATTEMPTS) {
            reportLog("Zgłoś potencjalny błąd");
            reportPotentialErrorReq = new ReportPotentialErrorReq(DEVICE_SN, STATION_NUMBER, date);

            Response errorResponse = given().spec(reqSpec)
                    .basePath(reportPotentialErrorPath)
                    .body(reportPotentialErrorReq)
                    .post();

            Assert.assertEquals(errorResponse.getStatusCode(), 200, "Błąd zgłoszenia potencjalnego błędu: ");
            Assert.assertEquals(errorResponse.jsonPath().getString("Status"), "200", "Błąd zgłoszenia potencjalnego błędu");
            reportLog("Zgłoszono potencjalny error urządzenia: " + DEVICE_SN + " Data: " + "12.12.2022");

        }
    }
}