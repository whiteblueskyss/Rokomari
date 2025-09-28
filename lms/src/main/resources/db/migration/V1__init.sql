-- USERS
CREATE TABLE IF NOT EXISTS users (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100)        NOT NULL,
    email       VARCHAR(120)        NOT NULL UNIQUE,
    role        VARCHAR(20)         NOT NULL  -- READER | ADMIN
);

-- AUTHORS
CREATE TABLE IF NOT EXISTS authors (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(120)        NOT NULL
);

-- BOOKS
CREATE TABLE IF NOT EXISTS books (
    id            BIGSERIAL PRIMARY KEY,
    title         VARCHAR(200)       NOT NULL,
    genre         VARCHAR(80),
    publish_date  DATE
);

-- REVIEWS
CREATE TABLE IF NOT EXISTS reviews (
    id        BIGSERIAL PRIMARY KEY,
    user_id   BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    book_id   BIGINT NOT NULL REFERENCES books(id) ON DELETE CASCADE,
    review    TEXT
);

-- MANY-TO-MANY: BOOKS <-> AUTHORS
CREATE TABLE IF NOT EXISTS book_authors (
    book_id    BIGINT NOT NULL REFERENCES books(id)   ON DELETE CASCADE,
    author_id  BIGINT NOT NULL REFERENCES authors(id) ON DELETE CASCADE,
    PRIMARY KEY (book_id, author_id)
);

-- Helpful indexes (reads will be common on these)
CREATE INDEX IF NOT EXISTS idx_reviews_book_id ON reviews(book_id);
CREATE INDEX IF NOT EXISTS idx_reviews_user_id ON reviews(user_id);
CREATE INDEX IF NOT EXISTS idx_book_title ON books(title);
CREATE INDEX IF NOT EXISTS idx_author_name ON authors(name);
