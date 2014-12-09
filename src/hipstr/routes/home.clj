(ns hipstr.routes.home
  (:require [compojure.core :refer :all]
            [hipstr.cookies :as cookies]
            [hipstr.layout :as layout]
            [hipstr.models.user-model :as u]
            [hipstr.util :as util]
            [hipstr.validators.user-validator :as v]
            [ring.util.response :as response]))

(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html"))

(defn foo-response [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body  (str "<html><body><dt>Go bowling?</dt>"
              "<dd>" (:go-bowling? request) "</dd></body></html>")})

(defn signup-page []
  (layout/render "signup.html"))

(defn signup-page-submit [user]
  (let [errors (v/validate-signup user)]
    (if (empty? errors)
      (do
        (u/add-user! user)
        (response/redirect "/signup-success"))
      (layout/render "signup.html" (assoc user :errors errors)))))

(defn login-page
  "Renders the login form."
  ([]
  (layout/render "login.html" {:username (cookies/remember-me)}))
  ([credentials]
    (if (apply u/auth-user (map credentials [:username :password]))
      (do (if (:remember-me credentials)
            (cookies/remember-me (:username credentials))
            (cookies/remember-me ""))
        (response/redirect "/albums/recently-added"))
      (layout/render "login.html" {:invalid-credentials? true}))))

(defn logout
  "Logs the user out of this session."
  []
  (u/invalidate-auth)
  (response/redirect "/"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" request (foo-response request))
  (GET "/login" [] (login-page))
  (POST "/login" [& login-form] (login-page login-form))
  (ANY "/logout" [] (logout))
  (GET "/signup" [] (signup-page))
  (POST "/signup" [& form] (signup-page-submit form))
  (GET "/signup-success" [] "Success!"))
