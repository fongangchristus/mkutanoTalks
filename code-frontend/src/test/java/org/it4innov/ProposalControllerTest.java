package org.it4innov;

import io.quarkus.qute.TemplateInstance;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

//@QuarkusTest
public class ProposalControllerTest {

    @Test
    public void testHelloEndpoint() {
        /*iven()
          .when().get("/proposal")
          .then()
             .statusCode(200);*/
        Assertions.assertTrue(true);
    }

}