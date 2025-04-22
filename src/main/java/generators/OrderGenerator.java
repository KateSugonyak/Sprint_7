package generators;

import models.order.Order;

public class OrderGenerator {
    public static Order getDefaulOrder() {

        final String firstName = "Константин";
        final String lastName = "Константинопльский";
        final String address = "Москва, Ленинградский пр., д.1 ";
        final  String metroStation = "Сокол";
        final String phone = "+79991112233";
        final int rentTime = 15;
        final String deliveryDate = "2025-04-13";
        final String comment = "No comment";

        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, null);
    }
}
