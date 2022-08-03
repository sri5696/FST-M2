package Activities;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

public class Activity1 {

    public String baseUrl = "https://petstore.swagger.io";
    public String id;

    @Test(priority = 1)
    public void postRequest() {

        RestAssured.baseURI = baseUrl;

        String reqBody = "{\n" + "  \"id\": 77232,\n" + "  \"name\": \"Riley\",\n" + "  \"status\": \"alive\"\n" + "}";

        Response res = given().contentType(ContentType.JSON).body(reqBody)
                .when().post("/v2/pet");

        res.getBody().prettyPrint();

        id = res.body().path("id").toString();

        res.then().body("id", equals(77232))
                .body("name", equals("Riley"))
                .body("status", equals("alive"));
    }

    @Test(priority = 2)
    public void getRequest() {

        String uri = baseUrl + "/v2/pet/{petId}";
        Response res = given().when().pathParams("petId",77232).get(uri);

        res.prettyPrint();

        res.then().body("id", equals(77232))
                .body("name", equals("Riley"))
                .body("status", equals("alive"));

    }

    @Test(priority = 3)
    public void deleteRequest() {

        String uri = baseUrl + "/v2/pet/{petId}";
        Response res = given().when().pathParams("petId",77232).delete(uri);

        res.prettyPrint();
        System.out.println("id = " +id);
        res.then().statusCode(200).body("message",equals(id)).body("code",equals(200));
    }
}