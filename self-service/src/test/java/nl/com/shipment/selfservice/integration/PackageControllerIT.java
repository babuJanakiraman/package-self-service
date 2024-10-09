package nl.com.shipment.selfservice.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.restassured.RestAssured;

import io.restassured.http.ContentType;
import nl.com.shipment.selfservice.packages.PackageService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;



@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class PackageControllerIT {

    @LocalServerPort
    private Integer port;

    @Autowired
    private PackageService packageService;

    private static WireMockServer wireMockServer;



    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8089));
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);

    }

    @AfterAll
    public static void teardown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }    }


    @BeforeEach
    void setUp() {
        RestAssured.port = port;

    }


    @Test
    void getAllReceivers_shouldReturnAllReceivers(){
        when()
                .get("/api/packages/receivers")
                .then()
                .statusCode(200)
                .body("size()", equalTo(8));


    }

    @Test
    void shouldSubmitNewPackage_success(){

        wireMockServer.stubFor(post(urlPathMatching("/shippingOrders")).withRequestBody(equalToJson("""
                        {
                          "packageName": "package21",
                          "postalCode": "3000GB",
                          "streetName": "Street1",
                          "receiverName": "Robert Swaak",
                          "packageSize": "L"
                        }
                        """))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));


        given().contentType(ContentType.JSON)
                .body("""
                                {
                                  "packageName": "package21",
                                  "weight": 2000,
                                  "receiverId": "10001",
                                  "senderId": "10002"
                                }

          """
                )
                .when()
                .post("/api/packages")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldSubmitNewPackage_validationError(){
        given().contentType(ContentType.JSON)
                .body("""
                                {
                                  "packageName": "",
                                  "weight": 2000,
                                  "receiverId": "10001",
                                  "senderId": "10002"
                                }

          """
                )
                .when()
                .post("/api/packages")
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("errorMessage", equalTo("Invalid request content."))
                .body("errors[0].field", equalTo("packageName"));
    }

    @Test
    void getPackage_shouldReturnPackageDetails(){

        wireMockServer.stubFor(get(urlPathMatching("/shippingOrders/3f6c794b-2c96-491e-81fb-a2f9731d02c4"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("""
                                {
                                     "packageId": "3f6c794b-2c96-491e-81fb-a2f9731d02c4",
                                     "packageName": "package1",
                                     "packageSize": "M",
                                     "postalCode": "1082PP",
                                     "streetName": "Gustav Mahlerlaan 10",
                                     "receiverName": "Robert Swaak",
                                     "orderStatus": "DELIVERED",
                                     "expectedDeliveryDate": "2024-10-18T22:00:00.000+00:00",
                                     "actualDeliveryDateTime": "2024-10-17T06:30:00.000+00:00"
                                 }
                                """)));

        when()
                .get("/api/packages/{packageId}", "3f6c794b-2c96-491e-81fb-a2f9731d02c4")
                .then()
                .statusCode(200)
                .body("packageId", equalTo("3f6c794b-2c96-491e-81fb-a2f9731d02c4"))
                .body("packageName", equalTo("package1"))
                .body("packageStatus", equalTo("DELIVERED"))
                .body("dateOfRegistration", equalTo("2024-10-12"))
                .body("dateOfReceipt", equalTo("2024-10-17"));

    }

    @Test
    void getPackage_NoPackageFound(){
        when()
                .get("/api/packages/{packageId}", "abc-123-gef")
                .then()
                .statusCode(500);

    }

    @Test
    void shouldFindAllPackageDetailsByIdWithStatus_Delivered() throws IOException {
        String requestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/shippingService/allPackageWithDeliveredStatus.json")));

        wireMockServer.stubFor(get(urlEqualTo("/shippingOrders?status=DELIVERED&offset=0&limit=10"))
                .willReturn(aResponse()
                        .withBody(requestBody)
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));

        given()
                .queryParam("offset", 0)
                .queryParam("limit", 10)
                .queryParam("status", "DELIVERED")
                .when()
                .get("api/packages/sender/{senderEmployeeId}", "20001")
                .then()
                .statusCode(200)
                .body("[0].packageId", equalTo("3f6c794b-2c96-491e-81fb-a2f9731d02c4"))
                .body("[0].packageName", equalTo("package1"))
                .body("[0].packageStatus", equalTo("DELIVERED"))
                .body("[0].dateOfRegistration", equalTo("2024-10-12"))
                .body("[0].dateOfReceipt", equalTo("2024-10-17"));
    }

    @Test
    void shouldFindAllPackageDetailsByIdWithoutStatus() throws IOException {
        String requestBody = new String(Files.readAllBytes(Paths.get("src/test/resources/shippingService/allPackageWithoutStatusInQuery.json")));

        wireMockServer.stubFor(get(urlEqualTo("/shippingOrders?offset=0&limit=10"))
                .willReturn(aResponse()
                        .withBody(requestBody)
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));

        given()
                .queryParam("offset", 0)
                .queryParam("limit", 10)
                .when()
                .get("api/packages/sender/{senderEmployeeId}", "20001")
                .then()
                .statusCode(200)
                .body("size()", equalTo(4));

    }
    @Test
    void shouldFindAllPackageDetailsByInvalidSenderId() throws IOException {

        given()
                .queryParam("offset", 0)
                .queryParam("limit", 10)
                .when()
                .get("api/packages/sender/{senderEmployeeId}", "abc123")
                .then()
                .statusCode(204);

    }


}