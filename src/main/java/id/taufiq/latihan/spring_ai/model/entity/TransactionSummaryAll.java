package id.taufiq.latihan.spring_ai.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummaryAll {

    private LocalDate transactionDate;
    private LocalDateTime latestDate;
    private String merchantUniqueCode;
    private String merchantName;
    private String merchantParentUniqueCode;
    private String merchantParentName;
    private String transStatus;

    private Integer countTrxAtome;
    private Integer countTrxCash;
    private Integer countTrxCashlezQrisDynamic;
    private Integer countTrxCashlezQrisStatic;
    private Integer countTrxCredit;
    private Integer countTrxDebit;
    private Integer countTrxDokuQris;
    private Integer countTrxEcomm;
    private Integer countTrxEligibility;
    private Integer countTrxGopayQr;
    private Integer countTrxIndodana;
    private Integer countTrxKredivoQr;
    private Integer countTrxNobuQr;
    private Integer countTrxNobuQrDynamic;
    private Integer countTrxOvoPushToPay;
    private Integer countTrxOvoQr;
    private Integer countTrxShopeePayQr;
    private Integer countTrxVaTransfer;

    private Long amountTrxAtome;
    private Long amountTrxCash;
    private Long amountTrxCashlezQrisDynamic;
    private Long amountTrxCashlezQrisStatic;
    private Long amountTrxCredit;
    private Long amountTrxDebit;
    private Long amountTrxDokuQris;
    private Long amountTrxEcomm;
    private Long amountTrxEligibility;
    private Long amountTrxGopayQr;
    private Long amountTrxIndodana;
    private Long amountTrxKredivoQr;
    private Long amountTrxNobuQr;
    private Long amountTrxNobuQrDynamic;
    private Long amountTrxOvoPushToPay;
    private Long amountTrxOvoQr;
    private Long amountTrxShopeePayQr;
    private Long amountTrxVaTransfer;
}
