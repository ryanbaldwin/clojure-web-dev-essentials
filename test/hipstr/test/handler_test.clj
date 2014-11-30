(ns hipstr.test.handler-test
  (:use clojure.test
        ring.mock.request
        hipstr.handler))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= 404 (:status response))))))

(deftest invalid-signup-parameter-redisplays-the-signup-form
  (let [response (app (request :post "/signup"
                               {:username "TheDude" :password "123456789"}))]
    (is (= 200 (:status response)))))
