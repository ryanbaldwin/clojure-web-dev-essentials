CREATE TABLE albums
(album_id     SERIAL      NOT NULL PRIMARY KEY,
 artist_id    BIGINT NOT NULL REFERENCES artists (artist_id),
 name         VARCHAR(255) NOT NULL,
 release_date DATE NOT NULL,
 created_at   TIMESTAMP   NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
 updated_at   TIMESTAMP   NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
 CONSTRAINT arist_album_name UNIQUE (artist_id, name));
--;;
-- create an update trigger which updates our update_date column by calling the above function
CREATE TRIGGER update_album_update_date BEFORE UPDATE
ON albums FOR EACH ROW EXECUTE PROCEDURE
update_updated_at();
--;;
INSERT INTO albums (artist_id, name, release_date)
  SELECT a.artist_id, 'My Iron Lung', '1978-11-24'
  FROM artists a
  WHERE a.name = 'The Arthur Digby Sellers Band'
--;;
INSERT INTO albums (artist_id, name, release_date)
  SELECT a.artist_id, 'American History Fail', '2000-04-18'
  FROM artists a
  WHERE a.name = 'The Arthur Digby Sellers Band'
--;;
INSERT INTO albums (artist_id, name, release_date)
  SELECT a.artist_id, 'Giggles and Mustaches', '1992-11-29'
  FROM artists a
  WHERE a.name = 'Fort Knox Harrington'
--;;
INSERT INTO albums (artist_id, name, release_date)
  SELECT a.artist_id, '20 Tons of Video Gold', '1990-10-09'
  FROM artists a
  WHERE a.name = 'Fort Knox Harrington'
--;;
INSERT INTO albums (artist_id, name, release_date)
  SELECT a.artist_id, 'Fixing the Cable', '1989-06-02'
  FROM artists a
  WHERE a.name = 'Hungus'
--;;
INSERT INTO albums (artist_id, name, release_date)
  SELECT a.artist_id, 'Over the Line', '1998-08-08'
  FROM artists a
  WHERE a.name = 'Smokey Fouler'
--;;
INSERT INTO albums (artist_id, name, release_date)
  SELECT a.artist_id, 'Petulant Suckup', '1995-05-21'
  FROM artists a
  WHERE a.name = 'Brant'
