import clients.CourierClient;
import io.qameta.allure.Description;
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
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static utils.QaScooterUrls.BASE_URL;

public class CourierTests {

    private CourierClient courierClient;
    private Integer courierId;
    private boolean isOk;
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        courierClient = new CourierClient();
    }

    @After
    public void tearTest() {
        if (courierId != null){
        courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Тест проверяет что курьера можно создать. Ожидаемый результат - курьер создан")
    public void createCourier() {
        Courier courier = randomCourier();

        Response response = courierClient.create(courier);
        isOk = response.as(CourierResponse.class).isOk();
        assertEquals("Некорректный код ответа", SC_CREATED, response.statusCode());
        assertEquals("Некорректный ответ", true, isOk);

        Response loginResponse = courierClient.login(credsFromCourier(courier));
        assertEquals("Некорректный код ответа при авторизации", SC_OK, loginResponse.statusCode());
        courierId = loginResponse.as(CourierResponse.class).getId();
    }

}
