(ns hipstr.models.album-model
  (:require [yesql.core :refer [defqueries]]
            [clojure.java.jdbc :as jdbc]
            [hipstr.models.connection :refer [db-spec]])
  (:use korma.db
        korma.core))

;(defqueries "hipstr/models/albums.sql" {:connection db-spec})
;(defqueries "hipstr/models/artists.sql" {:connection db-spec})

(declare artists albums)

; define our artists entity.
; by default korma assumes the entity and table name map
(defentity artists
  ; We must define the primary key because it does not
  ; adhere to the korma defaults.
  (pk :artist_id)

  ; define the relationship between artists and albums
  (has-many albums))

; define the albums entity
(defentity albums
  ; again, we have to map the primary key to our korma definition.
  (pk :album_id)

  ; We can define the foreign key relationship of the albums back
  ; to the artists table
  (belongs-to artists {:fk :artist_id}))

; -- name: get-recently-added
; -- Gets the 10 most recently added albums in the db.
; SELECT art.name as artist, alb.album_id, alb.name as album_name,
;        alb.release_date, alb.create_date
; FROM artists art
; INNER JOIN albums alb ON art.artist_id = alb.artist_id
; ORDER BY alb.create_date DESC
; LIMIT 10
(defn get-recently-added
  "Gets the 10 most recently added albums in the db."
  []
  (select albums
    (fields :album_id
	  [:name :album_name] :release_date :created_at)
    (with artists (fields [:name :artist]))
    (order :created_at :DESC)
    (limit 10)))

; -- name: get-by-artist
; -- Gets the discography for a given artist.
; SELECT alb.album_id, alb.name, alb.release_date
; FROM albums alb
; INNER JOIN artists art on alb.artist_id = art.artist_id
; WHERE
;   art.name = :artist
; ORDER BY alb.release_date DESC
(defn get-by-artist
  "Gets the discography for a given artist."
  ; for backwards compatibility it is expected that the
  ; artist param is a map, {:artist [value]}
  [artist]
  (select albums
    (join artists)
    ; for backwards compatibility we need to rename the :albums.name field to :album_name
    (fields :albums.album_id [:albums.name :album_name] :albums.release_date)
    (where {:artists.name (:artist artist)})
    (order :release_date :DESC)))

;-- name: insert-album<!
;-- Adds the album for the given artist to the database
;INSERT INTO albums (artist_id, name, release_date)
;VALUES (:artist_id, :album_name, date(:release_date))
(defn insert-album<!
  "Adds the album for the given artist to the database."
  ; for backwards compatibility it is expected that the
  ; album param is a map,
  ; {:artist_id :release_date :album_name :artist_name}
  ; As such we'll have to rename the :album_name key and remove
  ; the :artist_name.This is because korma will attempt to use all
  ; keys in the map when inserting, and :artist_name will destroy
  ; us with rabid vitriol.
  [album]
  (let [album (-> (clojure.set/rename-keys album {:album_name :name})
                  (dissoc :artist_name)
                  (assoc :release_date
			   (sqlfn date (:release_date album))))]
    (insert albums (values album))))

; -- name: get-album-by-name
; -- Fetches the specific album from the database for a particular
; -- artist.
; SELECT a.*
; FROM albums a
; WHERE
;   artist_id = :artist_id and
;   name = :album_name
(defn get-album-by-name
  "Fetches the specific album from the database for a particular
   artist."
  ; for backwards compatibility it is expected that the
  ; album param is {:artist_id :artist_name}
  [album]
  (first
   (select albums
          (where {:artist_id (:artist_id album)
                  :name (:artist_name album)}))))

; -- name: insert-artist<!
; -- Inserts a new artist into the database.
; INSERT INTO artists(name)
; VALUES (:artist_name)
(defn insert-artist<!
  "Inserts a new artist into the database."
  ; for backwards compatibility it is expected that the
  ; artist param is {:artist_name}
  [artist]
  (let [artist (clojure.set/rename-keys
			artist {:artist_name :name})]
    (insert artists (values artist))))

; -- name: get-artist-by-name
; -- Retrieves an artist from the database by name.
; SELECT *
; FROM artists
; WHERE name=:artist_name
(defn get-artist-by-name
  "Retrieves an artist from the database by name."
  ;for backwards compatibility it is expected that the
  ; artist_name param is {:artist_name}
  [artist_name]
  (first
   (select artists
           (where {:name (:artist_name artist_name)}))))


(defn add-album!
  "Adds a new album to the database."
  [album]
  (transaction
   (let [artist-info {:artist_name (:artist_name album)}
         ; fetch or insert the artist record
         artist (or (get-artist-by-name artist-info)
                    (insert-artist<! artist-info))
         album-info (assoc album :artist_id (:artist_id artist))]
     (or (get-album-by-name album-info)
         (insert-album<! album-info)))))
