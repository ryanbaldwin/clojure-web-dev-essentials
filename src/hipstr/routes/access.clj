(ns hipstr.routes.access
  (:require [hipstr.models.user-model :refer [is-authed?]]))

(def rules
  "The rules for accessing various routes in our application."
  [{:redirect "/login" :rule is-authed?}])
