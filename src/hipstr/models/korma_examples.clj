(ns hipstr.models.korma-examples
  (:require [korma.db :refer [defdb transaction]]
            [hipstr.models.connection :refer [db-spec]])
  (:use korma.core))

(defdb hipstr-db db-spec)
(declare artists albums)

(defentity artists
  (pk :artist_id)
  (has-many albums))

(defentity albums
  (pk :album_id)
  (belongs-to artists {:fk :artist_id}))

(select artists)

(select artists
        (fields :artist_id :name)
        (where (or {:name "Brant"}
                   {:name "Smokey Fouler"}))
        (where (not (= :updated_at :created_at))))

(select albums
        (order :created_at :DESC)
        (limit 10))

(select albums
        (with artists)
        (where {:artists.name "Brant"}))

(select albums
        (fields :album_id [:name :album_name]
                [:created_at :album_created_at]
                [:updated_at :album_updated_at])
        (with artists
              (fields [:name :artist_name]
                      [:created_at :artist_created_at]
                      [:updated_at :artist_updated_at]))
        (where {:artists.name "Brant"}))

#_(insert artists (values {:name "Maude Squad"}))

#_(insert artists (values {:name "Maude Squad"
                         :fake_column "Will destroy you."}))

(exec-raw ["SELECT art.name, count(*)
           FROM artists art
           INNER JOIN albums alb on art.artist_id = alb.artist_id
           GROUP BY art.name
           HAVING count(*) > ?" [1]] :results)

#_(transaction (delete artists (where {:name "Carlos Hungus"})))
