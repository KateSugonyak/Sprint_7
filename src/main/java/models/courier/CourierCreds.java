package models.courier;
import static utils.Utils.randomString;


public class CourierCreds {


    private String login;

    private String password;

    private CourierCreds(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierCreds credsFromCourier(Courier courier) {
        return new CourierCreds(courier.getLogin(), courier.getPassword());
    }

    public static CourierCreds credsFromCourierNoPassword(Courier courier) {
        return new CourierCreds(courier.getLogin(), "");
    }

    public static CourierCreds credsFromCourierNoLogin(Courier courier) {
        return new CourierCreds("", courier.getPassword());
    }

    public static CourierCreds credsFromCourierWrongLogin(Courier courier) {
        return new CourierCreds(courier.getLogin() + "BAD", courier.getPassword());
    }

    public static CourierCreds credsFromCourierWrongPassword(Courier courier) {
        return new CourierCreds(courier.getLogin(), courier.getPassword() + "BAD");
    }

    public static CourierCreds credsEmpty() {
        return new CourierCreds("", "");
    }

    public static CourierCreds credsWrong(Courier courier) {
        return new CourierCreds(courier.getLogin() + randomString(),
                courier.getPassword() + randomString());
    }

}
