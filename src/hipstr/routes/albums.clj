(ns hipstr.routes.albums
  (:require [compojure.core :refer :all]
            [hipstr.layout :as layout]
            [hipstr.models.album-model :as album]))

(defn recently-added-page
  "Renders the recently-added page."
  []
  (layout/render "albums/recently-added.html"
                 {:albums (album/get-recently-added)}))

(defn artist-albums-page
  "Renders the albums for a given artist."
  [artist]
  (layout/render "albums/artist-albums.html"
                 {:artist artist
                  :albums (album/get-by-artist {:artist artist})}))

(defroutes album-routes
  (GET "/albums/recently-added" [] (recently-added-page))
  (GET "/albums/:artist" [artist] (artist-albums-page artist)))
