package basic_api_calls.post_resource;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class SimpleApiTestExamples {

    /**
     * This method will check if JSON body has size 100
     */

    @Test
    public void shouldPostsSizeBe100() {

        given().
                when().
                get("https://jsonplaceholder.typicode.com/posts").
                then().
                assertThat().
                body("", hasSize(100));
    }

    /**
     * This method wil check if every userId postSize is equal and it's value is 10
     */

    @Test
    public void shouldEveryUserIdPostValueBeTheSame() {

        int postSize = 10;

        for (int userId = 1; userId <= 10; userId++) {

            given()
                    .when()
                    .get("https://jsonplaceholder.typicode.com/posts?userId=" + userId)
                    .then()
                    .assertThat()
                    .body("", hasSize(postSize));

            System.out.println("User " + userId + " test passed and has size " + postSize);
        }
    }

    /**
     * This method will check if specific userId has specific value in body response
     */

    @Test
    public void shouldUserId5HasItemTitle() {

        int userId = 5;

        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/posts?id=" + userId)
                .then()
                .statusCode(200)
                .assertThat()
                .body("title", hasItem("nesciunt quas odio"));
    }

    /**
     * This method will check if body response has specific items in extracted object
     */

    @Test
    public void shouldUserIdHasTenItems() {

        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/users")
                .then()
                .body("id", hasItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    /**
     * This method will check if body response has specific String value in extracted object
     */

    @Test
    public void shouldNameHasOneItemAtLeast() {

        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/users")
                .then()
                .body("name", hasItems("Leanne Graham"));
    }

    /**
     * You can easily send JSON body into post method. Just put requestParams into body method - 94 line. Please remember,
     * that contentType should be JSON. Additionally server should answer with 500 - why? This is fake sever for testing purposes.
     * But still you can check answer body with additional 101 user id here.
     */

    @Test
    public void shouldPostJsonBody() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com/posts";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("title", "foo");
        requestParams.put("body", "bar");
        requestParams.put("userId", 1);

        System.out.print("Response body is " + requestParams.toJSONString());
        request
                .body("")
                .post()
                .then()
                .statusCode(201)
                .extract()
                .response();
    }

    /**
     * This test will make request and assert that the titles of the posts with
     * a user id less than 54 are "qui est esse", "nesciunt quas odio"
     */

    @Test
    public void shouldTitleHasItemsInLimitedUserIds() {

        when()
                .get("https://jsonplaceholder.typicode.com/posts")
                .then()
                .body("findAll { it.id < 54 }.title", hasItems("qui est esse", "nesciunt quas odio"));
    }

    /**
     * This test will make request and assert validations on extracted objects"
     */

    @Test
    public void shouldCheckValuesInJSONResponse() {

        List<Map<String, Object>> posts = get("https://jsonplaceholder.typicode.com/posts?id=1")
                .as(new TypeRef<List<Map<String, Object>>>() {
                });

        assertThat(posts, hasSize(1));
        assertThat(posts.get(0).get("id"), equalTo(1));
        assertThat(posts.get(0).get("userId"), equalTo(1));
        assertThat(posts.get(0).get("title"), equalTo("sunt aut facere repellat provident occaecati excepturi" +
                " optio reprehenderit"));
        assertThat(posts.get(0).get("body"), equalTo("quia et suscipit\nsuscipit recusandae consequuntur expedita et" +
                " cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"));

    }
}