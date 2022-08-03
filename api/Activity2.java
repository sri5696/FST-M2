package Activities;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.*;

public class Activity2 {

    public String baseUrl = "https://petstore.swagger.io";
    public String id1,username;
    public int id;

    @Test(priority = 1)
    public void postRequest() throws Exception {

        RestAssured.baseURI = baseUrl;

        File file = new File("./src/test/java/org/resources/testDataActivity2.json");
        FileInputStream fis = new FileInputStream(file);
        byte[] by = new byte[(int) file.length()];
        fis.read(by);

        String reqBody = new String(by, "UTF-8");
        System.out.println(reqBody);
        JsonPath jsonReqBody = new JsonPath(reqBody);
        username = jsonReqBody.get("username");
        System.out.println("username = " + username);
        System.out.println(reqBody);

        Response res = given().contentType(ContentType.JSON).body(reqBody)
                .when().post("/v2/user");

        res.getBody().prettyPrint();

        id = Integer.parseInt(res.body().path("message").toString());
        //username = res.body().path("username").toString();

        res.then().statusCode(200);
    }

    @Test(priority = 2)
    public void getRequest() throws IOException {

        String uri = baseUrl + "/v2/user/{username}";
        Response res = given().when().pathParams("username", username).get(uri);

        res.then().log().status();

        String resBody = res.prettyPrint();

        File file = new File("./src/test/java/org/resources/Activity2Response.json");
        file.createNewFile();
        FileWriter wr = new FileWriter(file);
        wr.write(resBody);
        wr.close();

        res.then().body("id", equalTo(id))
                .body("username", equalTo(username))
                .body("firstName", equalTo("Justin"))
                .body("lastName", equalTo("Case"))
                .body("email", equalTo("justincase@mail.com"))
                .body("password", equalTo("password123"))
                .body("phone", equalTo("9812763450")).statusCode(200);
    }

    @Test(priority = 3)
    public void deleteRequest() {

        String uri = baseUrl + "/v2/user/{username}";
        Response res = given().when().pathParams("username", username).delete(uri);

        res.prettyPrint();
        System.out.println("useranme = " + username);
        res.then().body("code", equalTo(200)).body("message", equalTo(username));
    }
}