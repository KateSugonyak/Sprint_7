package clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.courier.Courier;
import models.courier.CourierCreds;
import models.courier.CourierResponse;

import static io.restassured.RestAssured.given;
import static utils.QaScooterUrls.*;

public class CourierClient {

    @Step("POST for create courier")
    public Response create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(COURIER);
    }

    @Step("POST for login courier")
    public Response login(CourierCreds courierCreds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierCreds)
                .when()
                .post(COURIER_LOGIN);
    }

    @Step("DELETE for delete courier by id")
    public Response delete(Integer id) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(COURIER + "/" + id);
    }

    @Step("DELETE for delete courier without id")
    public Response deleteWhithoutId() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(COURIER);
    }

    @Step("Get courier id")
    public Integer getCourierId(Response response) {
        return response.body().as(CourierResponse.class).getId();
    }


}
