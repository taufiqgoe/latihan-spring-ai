package id.taufiq.latihan.spring_ai.util;

import id.taufiq.latihan.spring_ai.model.entity.*;
import org.springframework.jdbc.core.RowMapper;

public final class Mapper {

    public static final RowMapper<AuthToken> AUTH_TOKEN_ROW_MAPPER = (rs, rowNum) -> new AuthToken(
            rs.getString("token"),
            rs.getString("username"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );
    public static final RowMapper<UserAccount> USER_ACCOUNT_ROW_MAPPER = (rs, rowNum) -> new UserAccount(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("roles"),
            rs.getBoolean("enabled")
    );
    public static final RowMapper<UserRateLimit> USER_RATE_LIMIT_ROW_MAPPER = (rs, rowNum) -> new UserRateLimit(
            rs.getString("username"),
            rs.getInt("max_requests"),
            rs.getInt("request_count"),
            rs.getDate("request_date").toLocalDate()
    );
    public static final RowMapper<TransactionSummaryAll> TRANSACTION_SUMMARY_ALL_ROW_MAPPER = (rs, rowNum) -> new TransactionSummaryAll(

            // === DATE ===
            rs.getDate("transaction_date") != null ? rs.getDate("transaction_date").toLocalDate() : null,
            rs.getTimestamp("latest_date") != null ? rs.getTimestamp("latest_date").toLocalDateTime() : null,

            // === MERCHANT INFO ===
            rs.getString("merchant_unique_code"),
            rs.getString("merchant_name"),
            rs.getString("merchant_parent_unique_code"),
            rs.getString("merchant_parent_name"),
            rs.getString("trans_status"),

            // === COUNT (Integer) ===
            rs.getInt("count_trx_atome"),
            rs.getInt("count_trx_cash"),
            rs.getInt("count_trx_cashlez_qris_dynamic"),
            rs.getInt("count_trx_cashlez_qris_static"),
            rs.getInt("count_trx_credit"),
            rs.getInt("count_trx_debit"),
            rs.getInt("count_trx_doku_qris"),
            rs.getInt("count_trx_ecomm"),
            rs.getInt("count_trx_eligibility"),
            rs.getInt("count_trx_gopay_qr"),
            rs.getInt("count_trx_indodana"),
            rs.getInt("count_trx_kredivo_qr"),
            rs.getInt("count_trx_nobu_qr"),
            rs.getInt("count_trx_nobu_qr_dynamic"),
            rs.getInt("count_trx_ovo_push_to_pay"),
            rs.getInt("count_trx_ovo_qr"),
            rs.getInt("count_trx_shopeepay_qr"),
            rs.getInt("count_trx_va_transfer"),

            // === AMOUNT (Long) ===
            rs.getLong("amount_trx_atome"),
            rs.getLong("amount_trx_cash"),
            rs.getLong("amount_trx_cashlez_qris_dynamic"),
            rs.getLong("amount_trx_cashlez_qris_static"),
            rs.getLong("amount_trx_credit"),
            rs.getLong("amount_trx_debit"),
            rs.getLong("amount_trx_doku_qris"),
            rs.getLong("amount_trx_ecomm"),
            rs.getLong("amount_trx_eligibility"),
            rs.getLong("amount_trx_gopay_qr"),
            rs.getLong("amount_trx_indodana"),
            rs.getLong("amount_trx_kredivo_qr"),
            rs.getLong("amount_trx_nobu_qr"),
            rs.getLong("amount_trx_nobu_qr_dynamic"),
            rs.getLong("amount_trx_ovo_push_to_pay"),
            rs.getLong("amount_trx_ovo_qr"),
            rs.getLong("amount_trx_shopeepay_qr"),
            rs.getLong("amount_trx_va_transfer")
    );
    public static final RowMapper<WebSearchHistory> WEB_SEARCH_HISTORY_ROW_MAPPER = (rs, rowNum) -> new WebSearchHistory(
            rs.getString("id"),
            rs.getString("query"),
            rs.getString("raw_request"),
            rs.getString("raw_response")
    );

    private Mapper() {
    }
}
