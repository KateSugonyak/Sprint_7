import clients.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static utils.QaScooterUrls.BASE_URL;

public class OrderGetListTests {
    private OrderClient orderClient;

    @Before
    @Step("Подготовка теста")
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка заказов.")
    @Description("Тест полверяет что список заказов приходит в ответ на запрос")
    public void getOrderList() {

        Response response = orderClient.getListOrders();
        assertEquals("Некорректный код ответа", SC_OK, response.statusCode());
        List<Map<String, Object>> orders = response.jsonPath().getList("orders");
        assertNotNull("Список заказов не должен быть null", orders);
    }
}
