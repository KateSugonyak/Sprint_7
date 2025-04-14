import clients.OrderClient;
import generators.OrderGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.order.Order;
import models.order.OrderResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.qameta.allure.Step;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static utils.QaScooterUrls.BASE_URL;

@RunWith(Parameterized.class)
@DisplayName("Тесты на создание заказа")
public class OrderCreateTests {

    private Integer orderTrack;
    private OrderClient orderClient;
    private Order order;
    private final List<String> color;

    public OrderCreateTests(List<String> color) {
        this.color = color;
    }
    @Parameterized.Parameters(name = "Scooter color - {0}")
    public static Object[][] initParamsForTest() {
        return new Object[][] {
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {List.of()}
        };
    }

    @Before
    @Step("Подготовка теста")
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        orderClient = new OrderClient();
        order = OrderGenerator.getDefaulOrder();
        orderTrack = null;
    }

    @After
    @Step("Удаление заказа после теста")
    public void tearDown() {
        if (orderTrack != null) {
            orderClient.cancelOrder(orderTrack);
                    //.then().statusCode(SC_OK); - метод не работает, временно убираю эту проверку
        }
    }


    @Test
    @DisplayName("Создание заказа с выбором цветов")
    @Description("Тест проверяет создание заказа с разными цветами и без указания цвета")
    public void createOrderWithDifferentColors() {

        order.setColor(color);
        Response createResponse = orderClient.createOrder(order);
        assertEquals("Некорректный код ответа при создании заказа", SC_CREATED, createResponse.statusCode());
        orderTrack = createResponse.as(OrderResponse.class).getTrack();
        assertTrue("Некорректный ответ. Должен вернуться номер заказа", orderTrack != null);
    }
}
