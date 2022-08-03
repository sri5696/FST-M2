package Activities;


import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;

public class RESTAssuredGitHubProject {

	
	public RequestSpecification reqSpec;
    String gitAccessToken = "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";
    String SSHKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAAAgQCQakTxEgJVMguOx7w8PbyzRhNDahRKRLG9n05GXWdtbqjEWnyiL+oPiI9VvUyFKz3O5I1zyrJw7NncR9AOPmOIS1nWS8nAqC2lZMjSUFXIFaUFVf546lykebUMT1/SnNAF+sAMn+f/b+qcKzOq1w8IOi2zQbuBK1sirnotWLoibQ==";
    int id;
    String sshK = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAAAgQCBEtjtm+9eO5rzseFj6s7S1jVlzMQPVtEo0to3IjRlDzF7I8sHRY6gbF/D6TQqN/CD7/9gBgBjH9f1dwpHOzn40MvZ94gr6uWu4LLT8TOTtlb7uFnbHrt8oazPlKdsqR8/mVoNOfH0c9+5urydJfZmpn0wpsr8iCU3YCtTE7pp8w==";

    @BeforeClass
    public void setUp() {

        reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAuth(oauth2(gitAccessToken))
                .setBaseUri("https://api.github.com")
                .setBasePath("/user/keys").build();
    }

    @Test(priority = 0)
    public void gitPostMethod() {

        String reqBody = "{ \"title\": \"TestAPIKey\",\n" +
                "    \"key\": \""+SSHKey+"\"\n}";

        Reporter.log("Request Body => "+reqBody, true);

        Response res = given()
                .spec(reqSpec).body(reqBody).log().uri()
                .when().post();

        Reporter.log(res.prettyPrint(), true);

        //id = res.getBody().path("id");
        id = res.then().extract().path("id");
        Reporter.log("id = " + id, true);

        res.then().statusCode(201);

    }

    @Test(priority = 1)
    public void gitGetMethod() {

        Response res = given().spec(reqSpec).when().get();

        //res.prettyPrint();
        Reporter.log(res.prettyPrint(), true);

        res.then().statusCode(200);

    }

    @Test(priority = 2)
    public void gitDeleteMethod() {

        Response res = given().spec(reqSpec).pathParams("keyId", id)
                .when().delete("{keyId}");

        System.out.println("________________________________________");
        res.then().log().all();
        System.out.println("________________________________________");

        Reporter.log(res.prettyPrint(), true);

        res.then().statusCode(204);
    }
	
}
