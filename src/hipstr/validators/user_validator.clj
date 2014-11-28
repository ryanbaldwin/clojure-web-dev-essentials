(ns hipstr.validators.user-validator
  (:require [validateur.validation :refer :all]))

(defn validate-signup [signup]
  "Validates teh incoming map of values from our signup form,
   and returns a set of error messages for any invalid key.
   Expects signup to have :username, :email, and :password."
  (let [v (validation-set
           (presence-of #{:email :password}
                        :message "is a required field.")
           (format-of :username
                      :format #"^[a-zA-Z0-9_]*$"
                      :message "Only letters, numbers, and underscores allowed."
                      :blank-message "is a required field")
           (length-of :password
                      :within (range 8 101)
                      :message-fn (fn [type m attribute & args]
                                    (if (= type :blank)
                                      "is a required field"
                                      "Password must be between 8 and 100 characters long.")))]
    (v signup)))
