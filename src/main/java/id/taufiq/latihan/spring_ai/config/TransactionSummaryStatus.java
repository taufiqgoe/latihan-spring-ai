package id.taufiq.latihan.spring_ai.config;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum TransactionSummaryStatus {
    SETTLED, VOID, EXPIRED_QRIS, PENDING, REVERSE, DECLINED, SUCCESSFUL
}
