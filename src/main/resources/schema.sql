CREATE TABLE IF NOT EXISTS user_rate_limits (
    username VARCHAR(255) PRIMARY KEY,
    max_requests INT NOT NULL,
    request_count INT NOT NULL,
    request_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS web_search_histories (
    id VARCHAR(255) PRIMARY KEY,
    query TEXT,
    raw_request TEXT,
    raw_response TEXT
);
