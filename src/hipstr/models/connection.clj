(ns hipstr.models.connection)

(def db-spec {:classname   "org.postgresql.Driver"
              :subprotocol "postgresql"
              :subname     "//localhost/postgres"
              :user        "hipstr"
              :password    "p455w0rd"})
