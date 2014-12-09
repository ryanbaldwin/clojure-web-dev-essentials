(ns hipstr.models.user-model
  (:require [yesql.core :refer [defqueries]]
            [crypto.password.bcrypt :as password]
            [hipstr.models.connection :refer [db-spec]]
            [noir.session :as session]))

(defqueries "hipstr/models/users.sql" {:connection db-spec})

(defn add-user!
  "Saves a user to the database."
  [user]
  (let [new-user (->> (password/encrypt (:password user))
                      (assoc user :password)
                      insert-user<!)]
    (dissoc new-user :pass)))

(defn is-authed?
  "Retruns false if the current request is anonymous;
   otherwise true."
  [_]
  (not (nil? (session/get :user_id))))

(defn auth-user
  "Validates a username/password and, if they match, adds the
   user_id to the session and returns the user map from
   the database. Otherwise nil."
  [username password]
  (let [user (first (get-user-by-username
                      {:username username}))]  ;#1
    (when (and user (password/check password (:password user))) ;#2
        (session/put! :user_id (:user_id user)) ;#3
        (dissoc user :password)))) ;#4

(defn invalidate-auth
  "Invalidates a user's current authenticated state."
  []
  (session/clear!))
