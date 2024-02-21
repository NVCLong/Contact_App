CREATE table users(

    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    enabled  boolean NOT NULL      ,

);
CREATE TABLE authorities(
    id serial primary key,
    user_id INT NOT NULL,
    authority varchar(255) NOT NULL,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
)
Create table customers(
    id serial primary key,
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    role varchar(255) NOT NULL
);

INSERT INTO customers (id,username, password, role)
VALUES (1, 'admin', 'admin', 'admin,user')