(ns hipstr.test.validators.user-validator-test
  (:require [hipstr.validators.user-validator :as uv])
  (:use clojure.test))

(defn validate-email [email]
  "Validates the provided email for us, and returns the
   set of validation messages for the email, if any."
  (:email (uv/email-validator {:email email})))

(deftest blank-email-returns-email-is-required-message
  (let [result (validate-email "")]
    (is (= 1 (count result)))
    (is (= "is a required field" (first result)))))
