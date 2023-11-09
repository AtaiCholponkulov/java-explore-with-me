CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(250) NOT NULL,
    email varchar(254) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS category (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS event (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation varchar(2000) NOT NULL,
    category_id BIGINT REFERENCES category(id),
    confirmed_requests INT,
    created_on TIMESTAMP NOT NULL,
    description varchar(7000) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    initiator_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    longitude FLOAT NOT NULL,
    latitude FLOAT NOT NULL,
    paid BOOLEAN DEFAULT FALSE,
    participant_limit INT NOT NULL DEFAULT 0,
    published_on TIMESTAMP,
    request_moderation BOOLEAN NOT NULL DEFAULT TRUE,
    state varchar(10) NOT NULL,
    title varchar(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS participation_request (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created TIMESTAMP NOT NULL,
    event_id BIGINT REFERENCES event(id) ON DELETE CASCADE,
    requester_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    status varchar(10) NOT NULL,
    CONSTRAINT unique_event_requester UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilation (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN NOT NULL DEFAULT FALSE,
    title varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilation_event (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    compilation_id BIGINT REFERENCES compilation(id) ON DELETE CASCADE,
    event_id BIGINT REFERENCES event(id) ON DELETE CASCADE,
    CONSTRAINT unique_compilation_event UNIQUE (compilation_id, event_id)
);

CREATE TABLE IF NOT EXISTS comment (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text varchar(500) NOT NULL,
    event_id BIGINT REFERENCES event(id) ON DELETE CASCADE,
    author_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    parent_comment_id BIGINT REFERENCES comment(id) ON DELETE CASCADE,
    created_on TIMESTAMP NOT NULL
);
