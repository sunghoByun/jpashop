package jpabook.jpashop.domain.exception;

public class NotEhoughStockException extends RuntimeException {
    public NotEhoughStockException() {
        super();
    }

    public NotEhoughStockException(String message) {
        super(message);
    }

    public NotEhoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEhoughStockException(Throwable cause) {
        super(cause);
    }

}
