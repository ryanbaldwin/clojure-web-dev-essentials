(ns hipstr.validators.user-validator
  (:require [noir.validation :as v]
            [validateur.validation :refer :all]))

(def email-validator
  (validation-set
   (validate-with-predicate :email
                            #(v/is-email? (:email %))
                            :message-fn (fn [validation-map]
                                          (if (v/has-value? (:email validation-map))
                                            "The email's format is incorrect"
                                            "is a required field")))))

(def username-validator
  (validation-set
   (format-of :username
              :format #"^[a-zA-Z0-9_]*$"
              :blank-message "is a required field"
              :message "Only letters, numbers, and underscores allowed.")))

(def password-validator
  (validation-set
   (length-of :password
              :within (range 8 101)
              :blank-message "is a required field."
              :message-fn (fn [type m attribute & args]
                            (if (= type :blank)
                              "is a required field"
                              "Passwords must be between 8 and 100
                               characters long.")))))

(defn validate-signup [signup]
  "Validates the incoming signup map and returns a set of error
   messages for any invalid field."
   ((compose-sets email-validator username-validator password-validator) signup))
