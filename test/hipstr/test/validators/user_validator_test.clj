(ns hipstr.test.validators.user-validator-test
  (:require [hipstr.validators.user-validator :as uv])
  (:use clojure.test))

(defn validate-email [email]
  "Validates the provided email for us, and returns the
   set of validation messages for the email, if any."
  (:email (uv/email-validator {:email email})))

(defn validate-username [username]
  "Validates the provided username for us, and returns the
   set of validation messages for the username, if any."
  (:username (uv/username-validator {:username username})))

(defn validate-password [password]
  "Validates the provided password for us, and returns the
   set of validation messages for the username, if any."
  (:password (uv/password-validator {:password password})))

 (defn assert-error-message
   "Asserts that a given error message set contains a single error messsage
    and matches an expected message."
   [expected errors]
   (is (= 1 (count errors)))
   (is (= expected (first errors))))

(deftest blank-email-returns-email-is-required-message
  (assert-error-message uv/email-blank-msg (validate-email "")))

(deftest invalid-email-returns-appropriate-message
  (assert-error-message uv/email-format-msg (validate-email "dude@bides.")))

(deftest valid-email-returns-0-messages
  (let [result (validate-email "dude@bides.net")]
    (is (= 0 (count result)))))

(deftest blank-username-returns-a-username-required-message
  (assert-error-message uv/username-blank-msg (validate-username "")))

(deftest invalid-username-returns-appropriate-message
  (assert-error-message uv/username-invalid-msg
                        (validate-username "Yea! Spaces! Illegal Characters!")))

(deftest valid-username-returns-0-messages
  (let [result (validate-username "TheDude")]
    (is (= 0 (count result)))))

(deftest password-must-be-at-least-8-characters-long
  (assert-error-message uv/password-invalid-msg (validate-password "123456")))

(deftest password-must-be-less-than-100-characters-long
  (let [pwd (clojure.string/join (repeat 101 "a"))]
    (assert-error-message uv/password-invalid-msg (validate-password pwd))))

(deftest blank-password-returns-a-password-required-message
  (assert-error-message uv/password-blank-msg (validate-password "")))

(deftest valid-albeit-crappy-password-returns-0-messages
  (let [result (validate-password "12345678")]
    (is (= 0 (count result)))))
