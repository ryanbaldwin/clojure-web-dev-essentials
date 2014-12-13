(ns hipstr.models.connection
  (:require [environ.core :refer [env]])
  (:use korma.db))

(def db-spec {:classname   (env :db-classname)
              :subprotocol (env :db-subprotocol)
              :subname     (env :db-subname)
              :user        (env :db-user)
              :password    (env :db-password)})

(defdb hipstr-db db-spec)
