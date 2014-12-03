CREATE TABLE users
(user_id     SERIAL      NOT NULL PRIMARY KEY,
 username    VARCHAR(30) NOT NULL,
 email       VARCHAR(60),
 password    VARCHAR(100),
 created_at  TIMESTAMP   NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
 updated_at  TIMESTAMP   NOT NULL DEFAULT (now() AT TIME ZONE 'utc'));
--;;
-- create a function which simply sets the update_date column to the current date/time.
CREATE OR REPLACE FUNCTION update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = now() AT TIME ZONE 'utc';
  RETURN NEW;
END
$$ language 'plpgsql';
--;;
-- create an update trigger which updates our update_date column by calling the above function
CREATE TRIGGER update_user_updated_at BEFORE UPDATE
ON users FOR EACH ROW EXECUTE PROCEDURE
update_updated_at();
