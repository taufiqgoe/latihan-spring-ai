CREATE TABLE IF NOT EXISTS user_rate_limits (
    username VARCHAR(255) PRIMARY KEY,
    max_requests INT NOT NULL,
    request_count INT NOT NULL,
    request_date DATE NOT NULL
);
