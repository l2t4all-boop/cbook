CREATE TABLE IF NOT EXISTS app_user (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    mobile VARCHAR(20),
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS contact (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    mobile VARCHAR(20) NOT NULL,
    dob DATE,
    user_id UUID NOT NULL,
    CONSTRAINT fk_contact_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);
