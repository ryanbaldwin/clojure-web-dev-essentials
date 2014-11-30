(ns hipstr.validators.user-validator
  (:require [noir.validation :as v]
            [validateur.validation :refer :all]))

(def email-blank-msg
  "Email is a required field")

(def email-format-msg
  "The email's format is incorrect")

(def email-validator
  (validation-set
   (validate-with-predicate :email
                            #(v/is-email? (:email %))
                            :message-fn (fn [validation-map]
                                          (if (v/has-value? (:email validation-map))
                                            email-format-msg
                                            email-blank-msg)))))

(def username-blank-msg
  "Username is a required field")

(def username-invalid-msg
  "Only letters, numbers, and underscores allowed.")

(def username-validator
  (validation-set
   (format-of :username
              :format #"^[a-zA-Z0-9_]*$"
              :blank-message username-blank-msg
              :message username-invalid-msg)))

(def password-blank-msg
  "Password is a required field")

(def password-invalid-msg
  "Passwords must be between 8 and 100 characters long.")

(def password-validator
  (validation-set
   (length-of :password
              :within (range 8 101)
              :message-fn (fn [type m attribute & args]
                            (if (= type :blank)
                              password-blank-msg
                              password-invalid-msg)))))

(defn validate-signup [signup]
  ((compose-sets email-validator username-validator password-validator) signup))
