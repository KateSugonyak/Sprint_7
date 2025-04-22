package models.courier;

public class CourierResponse {


    private Integer id;
    private boolean ok;
    private String message;

    public CourierResponse(int id, boolean ok, String message) {
        this.id = id;
        this.ok = ok;
        this.message = message;
    }

    public CourierResponse() {
    }

    public int getId() {

        return id;
    }
    public boolean isOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }
}
