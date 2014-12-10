(ns hipstr.models.connection
  (:require [environ.core :refer [env]]))

(def db-spec {:classname   "org.postgresql.Driver"
              :subprotocol "postgresql"
              :subname     "//localhost/postgres"
              :user        "hipstr"
              :password    "p455w0rd"})
