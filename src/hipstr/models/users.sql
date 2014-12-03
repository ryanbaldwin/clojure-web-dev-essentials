-- name: insert-user<!
-- Inserts a new user into the Users table
-- Expects :username, :email, and :password
INSERT INTO users (username, email, password)
VALUES (:username, :email, :password)
