CREATE TABLE IF NOT EXISTS endpoint_hit (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app varchar(100) NOT NULL,
    uri varchar(100) NOT NULL,
    ip varchar(50) NOT NULL,
    date_time TIMESTAMP
);
