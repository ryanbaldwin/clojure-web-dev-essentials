(ns hipstr.models.user-model
  (:require [yesql.core :refer [defqueries]]
            [crypto.password.bcrypt :as password]
            [hipstr.models.connection :refer [db-spec]]))

(defqueries "hipstr/models/users.sql" {:connection db-spec})

(defn add-user!
  "Saves a user to the database."
  [user]
  (let [new-user (->> (password/encrypt (:password user))
                      (assoc user :password)
                      insert-user<!)]
    (dissoc new-user :pass)))
