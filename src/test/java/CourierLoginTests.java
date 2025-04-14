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

import static generators.CourierGenerator.randomCourier;
import static models.courier.CourierCreds.*;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static utils.QaScooterUrls.BASE_URL;

@DisplayName("Тесты на авторизацию курьера")
public class CourierLoginTests {
    private CourierClient courierClient;
    private Integer courierId;
    private String message;
    private Courier courier;
    @Before
    @Step("Подготовка данных для теста")
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        courierClient = new CourierClient();
        courierId = null;
        message = null;
        courier = randomCourier();
        courierClient.create(courier).then().statusCode(SC_CREATED);
    }

    @After
    @Step("Удаление курьера после теста")
    public void tearDown() {
        if (courierId != null){
            courierClient.delete(courierId).then().statusCode(SC_OK);
        }
    }

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Тест проверяет что курьер может залогиниться")
    public void loginCourier() {
        Response loginResponse = courierClient.login(credsFromCourier(courier));
        assertEquals("Некорректный код ответа", SC_OK, loginResponse.statusCode());
        courierId = loginResponse.as(CourierResponse.class).getId();
        assertNotNull("Некорректное значение id", courierId);
    }

    @Test
    @DisplayName("Авторизация курьера без кредов")
    @Description("Тест проверяет что курьер не может залогиниться без логина и пароля")
    public void loginCourierNoCreds() {
        Response loginResponse = courierClient.login(credsEmpty());
        message = loginResponse.as(CourierResponse.class).getMessage();
        assertEquals("Некорректный код ответа", SC_BAD_REQUEST, loginResponse.statusCode());
        assertEquals("Некорректный ответ", "Недостаточно данных для входа", message);
    }
    @Test
    @DisplayName("Авторизация курьера без логина")
    @Description("Тест проверяет что курьер не может залогиниться без логина")
    public void loginCourierNoLogin() {
        Response loginResponse = courierClient.login(credsFromCourierNoLogin(courier));
        message = loginResponse.as(CourierResponse.class).getMessage();
        assertEquals("Некорректный код ответа", SC_BAD_REQUEST, loginResponse.statusCode());
        assertEquals("Некорректный ответ", "Недостаточно данных для входа", message);
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    @Description("Тест проверяет что курьер не может залогиниться без пароля")
    public void loginCourierNoPassword() {
        Response loginResponse = courierClient.login(credsFromCourierNoPassword(courier));
        message = loginResponse.as(CourierResponse.class).getMessage();
        assertEquals("Некорректный код ответа", SC_BAD_REQUEST, loginResponse.statusCode());
        assertEquals("Некорректный ответ", "Недостаточно данных для входа", message);
    }

    @Test
    @DisplayName("Авторизация курьера c неправильным логином")
    @Description("Тест проверяет что курьер не может залогиниться без пароля")
    public void loginCourierWrongLogin() {
        Response loginResponse = courierClient.login(credsFromCourierWrongLogin(courier));
        message = loginResponse.as(CourierResponse.class).getMessage();
        assertEquals("Некорректный код ответа", SC_NOT_FOUND, loginResponse.statusCode());
        assertEquals("Некорректный ответ", "Учетная запись не найдена", message);
    }

    @Test
    @DisplayName("Авторизация курьера с неправильным паролем")
    @Description("Тест проверяет что курьер не может залогиниться без пароля")
    public void loginCourierWrongPassword() {
        Response loginResponse = courierClient.login(credsFromCourierWrongPassword(courier));
        message = loginResponse.as(CourierResponse.class).getMessage();
        assertEquals("Некорректный код ответа", SC_NOT_FOUND, loginResponse.statusCode());
        assertEquals("Некорректный ответ", "Учетная запись не найдена", message);
    }

    @Test
    @DisplayName("Авторизация несуществуюзего курьера")
    @Description("Тест проверяет что незапегистрированный курьер не может залогинитьсяя")
    public void loginCourierWrongCreds() {
        Response loginResponse = courierClient.login(credsWrong(courier));
        message = loginResponse.as(CourierResponse.class).getMessage();
        assertEquals("Некорректный код ответа", SC_NOT_FOUND, loginResponse.statusCode());
        assertEquals("Некорректный ответ", "Учетная запись не найдена", message);
    }

}
