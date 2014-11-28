(ns hipstr.routes.home
  (:require [compojure.core :refer :all]
            [hipstr.layout :as layout]
            [hipstr.util :as util]
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
  #_(let [errors (signup/validate-signup user)]
    (if (empty? errors)
      (response/redirect "/signup-success.html")
      (layout/render "signup.html" (assoc user :errors errors)))))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" request (foo-response request))
  (GET "/signup" [] (signup-page))
  (POST "/signup" [& form] (signup-page-submit form))
  (GET "/signup-success" [] "Success!"))
