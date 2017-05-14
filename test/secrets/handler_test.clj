(ns secrets.handler-test
  (:require [clojure.test :refer :all]
            [clojure.pprint :as pp]
            [ring.mock.request :as mock]
            [secrets.handler :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
