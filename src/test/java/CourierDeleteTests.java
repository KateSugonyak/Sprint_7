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
import static models.courier.CourierCreds.credsFromCourier;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static utils.QaScooterUrls.BASE_URL;

@DisplayName("Тесты на удаление курьера")
public class CourierDeleteTests {

    private CourierClient courierClient;
    private Integer courierId;
    private boolean isOk;
    private String message;
    private Courier courier;
    @Before
    @Step("Подготовка данных для теста")
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        courierClient = new CourierClient();
        courierId = null;
        message = null;
        isOk = false;
        courier = randomCourier();
        Response createResponse = courierClient.create(courier);
        createResponse.then().statusCode(SC_CREATED);
        courierId = courierClient.getCourierId(courierClient.login(credsFromCourier(courier)));
    }

    @After
    @Step("Удаление курьера после теста")
    public void tearDown() {
        if (courierId != null){
            courierClient.delete(courierId).then().statusCode(SC_OK);
        }
    }

    @Test
    @DisplayName("Удаление курьера")
    @Description("Тест проверяет что курьера можно удалить")
    public void deleteCourier() {
        Response response = courierClient.delete(courierId);
        isOk = response.as(CourierResponse.class).isOk();
        assertEquals("Некорректный код ответа", SC_OK, response.statusCode());
        assertEquals("Некорректный ответ", true, isOk);
        courierId = null;
    }

    @Test  //этот тест падает, т.к. ответ не соответсвует спецификации
    @DisplayName("Удаление курьера без id")
    @Description("Тест проверяет что без id курьера удалеие возвращает ошибку")
    public void deleteCourierWithoutId() {
        Response response = courierClient.deleteWhithoutId();
        assertEquals("Некорректный код ответа", SC_BAD_REQUEST, response.statusCode());
        message = response.as(CourierResponse.class).getMessage();
        assertEquals("Некорректный ответ", "Недостаточно данных для удаления курьера", message);
    }

    @Test
    @DisplayName("Удаление курьера с несуществующим id")
    @Description("Тест проверяет что с несузествующим id курьера удалеие возвращает ошибку")
    public void deleteCourierWithWrongId() {
        Response response = courierClient.delete(-1);
        assertEquals("Некорректный код ответа", SC_NOT_FOUND, response.statusCode());
        message = response.as(CourierResponse.class).getMessage();
        assertEquals("Некорректный ответ", "Курьера с таким id нет.", message);
    }
}
