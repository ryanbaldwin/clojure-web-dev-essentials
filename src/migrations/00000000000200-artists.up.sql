CREATE TABLE artists					-- #1
( artist_id SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  updated_at TIMESTAMP NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  CONSTRAINT artist_name UNIQUE(name));
--;;
-- create an update trigger which updates our update_date -- column by calling the above function
CREATE TRIGGER update_artist_updated_at BEFORE UPDATE --#2
ON artists FOR EACH ROW EXECUTE PROCEDURE
update_updated_at();
--;;
INSERT INTO artists (name) VALUES ('The Arthur Digby Sellers Band');
--;;
INSERT INTO artists (name) VALUES ('Fort Knox Harrington');
--;;
INSERT INTO artists (name) VALUES ('Hungus');
--;;
INSERT INTO artists (name) VALUES ('Smokey Fouler');
--;;
INSERT INTO artists (name) VALUES ('Brant');
