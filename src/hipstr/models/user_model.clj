(ns hipstr.models.user-model
  (:require #_[yesql.core :refer [defqueries]]
            [crypto.password.bcrypt :as password]
            [hipstr.models.connection :refer [db-spec]]
            [noir.session :as session])
  (:use [korma.core]))

;(defqueries "hipstr/models/users.sql" {:connection db-spec})

; declare our users table, which in our hipstr application
; is pretty straight forward.
; For Korma, however, we have to define the primary key because
; the name of the primary key is neither 'id' or 'users_id'
; ([tablename]_id)
(defentity users
  (pk :user_id))

; -- name: get-user-by-username
; -- Fetches a user from the DB based on username.
; SELECT *
; FROM users
; WHERE username=:username
 (defn get-user-by-username
  "Gets a user by from the database by username."
  [username]
  (select users (where username)))

; -- name: insert-user<!
; -- Inserts a new user into the Users table
; -- Expects :username, :email, and :password
; INSERT INTO users (username, email, pass)
; VALUES (:username, :email, :password)
(defn insert-user<!
  "Inserts a user into our users table."
  [user]
  (insert users (values user)))

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
                      {:username username}))] ;#1
    (when (and user (password/check password (:password user))) ;#2
        (session/put! :user_id (:user_id user)) ;#3
        (dissoc user :password)))) ;#4

(defn invalidate-auth
  "Invalidates a user's current authenticated state."
  []
  (session/clear!))
