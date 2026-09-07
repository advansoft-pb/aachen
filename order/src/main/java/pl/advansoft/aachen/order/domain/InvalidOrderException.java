package pl.advansoft.aachen.order.domain;

public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super(message);
    }

    public static InvalidOrderException forCode(String code) {
        return new InvalidOrderException("Invalid Product code: " + code);
    }

    public static InvalidOrderException forPrice() {
        return new InvalidOrderException("Product price not matching");
    }
}
