package services;

public class ErrorClass{

    private String message;
    private int id;

    public ErrorClass () {
    }

    public ErrorClass (int id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getMessage () {
        return this.message;
    }

    public int getId () {
        return this.id;
    }
}