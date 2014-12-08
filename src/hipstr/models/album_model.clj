(ns hipstr.models.album-model
  (:require [clojure.java.jdbc :as jdbc]
            [yesql.core :refer [defqueries]]
            [hipstr.models.connection :refer [db-spec]]))

(defqueries "hipstr/models/albums.sql" {:connection db-spec})
(defqueries "hipstr/models/artists.sql" {:connection db-spec})

(defn add-album!
  "Adds a new album to the database."
  ([album]
   (jdbc/with-db-transaction [tx db-spec]
     (add-album! album tx)))
  ([album tx]
     (let [artist-info {:artist_name (:artist_name album)}
           txn {:connection tx}
           ;fetch or insert the artist record
           artist (or (first (get-artists-by-name artist-info txn))
                      (insert-artist<! artist-info txn))
           album-info (assoc album :artist_id (:artist_id artist))]
       (or (first (get-albums-by-name album-info txn))
           (insert-album<! album-info txn)))))
