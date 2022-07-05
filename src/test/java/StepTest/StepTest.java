package StepTest;

import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static Utils.Configuration.getFromProperties;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class StepTest {
    public String lastEpisode;
    public String lastCharacter;
    public String ourName;
    public String ourRace;
    public String ourLocation;
    public String raceMorty;
    public String locMorty;
    public String nameOurC;


    @DisplayName("Первый тест по Рику и Морти")
    @Когда("^выбрали персонажа$")
    public void morty(@NotNull List<String> id) {
        Response data1 = given()
                .header("Content-type", "application/json")
                .header("charset", "UTF-8")
                .baseUri(getFromProperties("apiUrl"))
                .when()
                .get("api/character/" + id.get(0))
                .then()
                .extract()
                .response();

        JSONObject infoMorty = new JSONObject(data1.getBody().asString());
        JSONArray episodeMorty = infoMorty.getJSONArray("episode");
        int countEpisodes = episodeMorty.length();
        lastEpisode = episodeMorty.getString(countEpisodes - 1);
        String subStr = lastEpisode.substring(40);
        nameOurC = infoMorty.getString("name");
        raceMorty = infoMorty.getString("species");
        locMorty = infoMorty.getJSONObject("location").getString("name");
    }
    @Тогда("^получили персонажа из последнего эпизода$")
    public void episode() {
        Response data2 = given()
                .header("Content-type", "application/json")
                .header("charset", "UTF-8")
                .baseUri("https://rickandmortyapi.com/")
                .when()
                .get(lastEpisode)
                .then()
                .extract()
                .response();

        JSONObject lastEpisodeMorty = new JSONObject(data2.getBody().asString());
        JSONArray characterEpisode = lastEpisodeMorty.getJSONArray("characters");
        int countCharacter = characterEpisode.length();
        lastCharacter = characterEpisode.getString(countCharacter - 1);
    }
    
    @Тогда("^получили информацию по персонажу из последнего эпизода$")
    public void character() {
        Response data3 = given()
                .header("Content-type", "application/json")
                .header("charset", "UTF-8")
                .baseUri("https://rickandmortyapi.com/")
                .when()
                .get(lastCharacter)
                .then()
                .extract()
                .response();

        JSONObject ourCharacter = new JSONObject(data3.getBody().asString());
        ourName = ourCharacter.getString("name");
        ourRace = ourCharacter.getString("species");
        ourLocation = ourCharacter.getJSONObject("location").getString("name");
    }
    @Тогда("^сравнили расы первого и второго персонажа$")
    public void result() {
        assertEquals("Расы персонажей разные", raceMorty, ourRace);
    }
    @Тогда("^сравнили локации персонажей$")
        public void loc() {
        assertNotEquals("Локации персонажей равны", locMorty, ourLocation);
    }

    @DisplayName("Второй тест по работе с сервером")
    @Когда("^переименовали объект и получили данные с сервера$")
    public void testPotato() throws IOException {

        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/potato.json"))));
        body.put("name", "Tomato");
        body.put("job", "Eat maket");

        Response renamePotato = given()
                .header("Content-type", "application/json")
                .header("charset", "UTF-8")
                .baseUri(getFromProperties("regresApi"))
                .body(body.toString())
                .post("/api/users")
                .then()
                .statusCode(201)
                .log().all()
                .extract().response();

            JSONObject jsonPotato = new JSONObject(renamePotato.getBody().asString());
            assertEquals(jsonPotato.getString("name"), "Tomato");
            assertEquals(jsonPotato.getString("job"), "Eat maket");
    }
}
