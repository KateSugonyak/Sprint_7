package clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.order.Order;

import static io.restassured.RestAssured.given;
import static utils.QaScooterUrls.*;

public class OrderClient {
    @Step("POST create order")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(ORDER);
    }

    @Step("GET orders list")
    public Response getListOrders() {
        return given()
                .get(ORDER);
    }

    @Step("PUT cancel order")
    public Response cancelOrder(int orderTrack) {

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(String.format("{\"track\":%d}", orderTrack))
                .when()
                .put(ORDER_CANCEL);
    }
/*
    @Step("GET order by track")
    public Response getOrder(String orderTrack) {
        return given()
                .queryParam("t", orderTrack)
                .when()
                .get(ORDER_TRACK);
    }
    @Step("PUT accept order")
    public Response acceptOrder(String orderTrack, String courierId) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .queryParam("courierId", courierId)
                .when()
                .put(ORDER_ACCEPT + orderTrack);
    }
*/
}
