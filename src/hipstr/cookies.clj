(ns hipstr.cookies
  (:require [noir.cookies :as c]))

(defn remember-me
  "Gets the username in the remember-me cookie."
  ([]
  (c/get :remember-me))
  ([username]
   "Sets a remember-me cookie to the user's browser with the
    user's username."
   (if username
     (c/put! :remember-me {:value username :path "/" :max-age (* 60 60 24 365)})
     (c/put! :remember-me {:value "" :path "/" :max-age -1}))))
