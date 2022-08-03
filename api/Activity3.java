package Activities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Activity3 {

    RequestSpecification reqSpec;
    ResponseSpecification resSpec;
    public String baseUrl = "https://petstore.swagger.io/v2/pet";

    @BeforeClass
    public void setUp() {

        reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(baseUrl)
                .build();

        resSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .expectBody("status", equalTo("alive"))
                .build();
    }

    @DataProvider
    public Object getTestData() {

        Object[][] data = new Object[][]{{77232, "Riley", "alive"},
                {77233, "Hansel", "alive"}};
        return data;
    }

    @Test(priority = 1)
    public void postRequest() {

        String reqBody1 = "{\"id\": 77232, \"name\": \"Riley\", \"status\": \"alive\"}\n";
        String reqBody2 = "{\"id\": 77233, \"name\": \"Hansel\", \"status\": \"alive\"}";

        Response res1 = given().spec(reqSpec).body(reqBody1)
                .when().post();

        Response res2 = given().spec(reqSpec).body(reqBody2)
                .when().post();

        res1.prettyPrint();
        res2.prettyPrint();

        res1.then().spec(resSpec);
        res2.then().spec(resSpec);
    }

    @Test(priority = 2, dataProvider = "getTestData")
    public void getRequest(int id, String name, String status) {

        Response res = given().spec(reqSpec).pathParams("id", id)
                .when().get("{id}");

        res.prettyPrint();

        res.then().spec(resSpec)
                .body("name", equalTo(name))
                .body("id", equalTo(id));

    }

    @Test(priority = 3 , dataProvider = "getTestData")
    public void deleteRequest(int id, String name, String status) {

        Response res = given().spec(reqSpec)
                .when().pathParam("id", id)
                .delete("{id}");

        res.prettyPrint();

        String expectedId = Integer.toString(id);
        res.then().statusCode(200).body("message", equalTo(expectedId));
    }
}