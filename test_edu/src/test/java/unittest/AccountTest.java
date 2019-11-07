package unittest;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @Test
    void 잔액이_10000원이_있는_계좌에서_5000원_출금시_남은잔액이_5000원인지_테스트() {
        Account account = new Account(10000);
        Account other = new Account();

        account.withdraw(other, 5000);

        assertThat(account.getBalance()).isEqualTo(5000);
        assertThat(other.getBalance()).isEqualTo(5000);
    }
}