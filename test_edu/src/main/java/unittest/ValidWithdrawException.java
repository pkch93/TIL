package unittest;

public class ValidWithdrawException extends RuntimeException {

    private static final String ERROR_MESSAGE = "잔액부족으로 출금이 불가능합니다.";

    public ValidWithdrawException() {
        super(ERROR_MESSAGE);
    }
}
