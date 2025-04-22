package generators;

import models.courier.Courier;

import static utils.Utils.randomString;

public class CourierGenerator {


    public static Courier randomCourier() {
        return new Courier()
                .setLogin(randomString())
                .setPassword(randomString())
                .setFirstName(randomString());
    }

    public static Courier emptyCourier() {
        return new Courier()
                .setLogin("")
                .setPassword("")
                .setFirstName("");
    }

    public static Courier noLoginCourier() {
        return new Courier()
                .setLogin("")
                .setPassword(randomString())
                .setFirstName(randomString());
    }

    public static Courier noPasswordCourier() {
        return new Courier()
                .setLogin(randomString())
                .setPassword("")
                .setFirstName(randomString());
    }

    public static Courier sameLoginCourier(String login) {
        return new Courier()
                .setLogin(login)
                .setPassword(randomString())
                .setFirstName(randomString());
    }
}
