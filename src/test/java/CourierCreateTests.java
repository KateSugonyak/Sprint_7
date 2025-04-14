import clients.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.courier.Courier;
import models.courier.CourierResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static generators.CourierGenerator.*;
import static models.courier.CourierCreds.credsFromCourier;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static utils.QaScooterUrls.BASE_URL;
@DisplayName("Тесты на создание курьера")
public class CourierCreateTests {

    private CourierClient courierClient;
    private Integer courierId;
    private boolean isOk;
    private String message;
    @Before
    @Step("Подготовка теста")
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        courierClient = new CourierClient();
        courierId = null;
        message = null;
        isOk = false;
    }

    @After
    @Step("Удаление курьера после теста")
    public void tearDown() {
        if (courierId != null){
        courierClient.delete(courierId).then().statusCode(SC_OK);
        }
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Тест проверяет что курьера можно создать")
    public void createCourier() {
        Courier courier = randomCourier();

        Response response = courierClient.create(courier);
        isOk = response.as(CourierResponse.class).isOk();
        assertEquals("Некорректный код ответа", SC_CREATED, response.statusCode());
        assertEquals("Некорректный ответ", true, isOk);

        Response loginResponse = courierClient.login(credsFromCourier(courier));
        courierId = loginResponse.as(CourierResponse.class).getId();
    }

    //фактический ответ не соответствует спецификации, тест будет падать
    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Тест проверяет что нельзя создать двух одинаковых курьеров")
    public void createSameCouriers() {
        Courier courier = randomCourier();
        courierClient.create(courier).then().statusCode(SC_CREATED).body("ok", equalTo(true));
        Response loginResponse = courierClient.login(credsFromCourier(courier));
        courierId = loginResponse.as(CourierResponse.class).getId();

        Response response = courierClient.create(courier);
        message = response.as(CourierResponse.class).getMessage();
        assertEquals("Некорректный код ответа", SC_CONFLICT, response.statusCode());
        assertEquals("Некорректный ответ", "Этот логин уже используется", message);
    }

    @Test
    @DisplayName("Создание курьера без данных")
    @Description("Тест проверяет что нельзя создать курьера без данных")
    public void createEmptyCourier() {
        Courier courier = emptyCourier();

        Response response = courierClient.create(courier);
        message = response.as(CourierResponse.class).getMessage();
        assertEquals("Некорректный код ответа", SC_BAD_REQUEST, response.statusCode());
        assertEquals("Некорректный ответ", "Недостаточно данных для создания учетной записи", message);
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Тест проверяет что нельзя создать курьера без логина")
    public void createNoLoginCourier() {
        Courier courier = noLoginCourier();

        Response response = courierClient.create(courier);
        message = response.as(CourierResponse.class).getMessage();
        assertEquals("Некорректный код ответа", SC_BAD_REQUEST, response.statusCode());
        assertEquals("Некорректный ответ", "Недостаточно данных для создания учетной записи", message);
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Тест проверяет что нельзя создать курьера без пароля")
    public void createNoPasswordCourier() {
        Courier courier = noPasswordCourier();

        Response response = courierClient.create(courier);
        message = response.as(CourierResponse.class).getMessage();
        assertEquals("Некорректный код ответа", SC_BAD_REQUEST, response.statusCode());
        assertEquals("Некорректный ответ", "Недостаточно данных для создания учетной записи", message);
    }

    //фактический ответ не соответствует спецификации, тест будет падать
    @Test
    @DisplayName("Создание курьера с существующим логином")
    @Description("Тест проверяет что нельзя создать курьера повторяющимся логином")
    public void createSameLoginCourier() {
        Courier courier = randomCourier();
        courierClient.create(courier).then().statusCode(SC_CREATED).body("ok", equalTo(true));
        Response loginResponse = courierClient.login(credsFromCourier(courier));
        courierId = loginResponse.as(CourierResponse.class).getId();

        Courier courierWithSameLogin = sameLoginCourier(courier.getLogin());
        Response response = courierClient.create(courierWithSameLogin);
        message = response.as(CourierResponse.class).getMessage();
        assertEquals("Некорректный код ответа", SC_CONFLICT, response.statusCode());
        assertEquals("Некорректный ответ", "Этот логин уже используется", message);

    }
}
