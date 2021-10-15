(ns demo.api-test
  (:require [clojure.test :refer [deftest is testing]]
            [demo.backend.api :as api]))

(def valid-request {:})

(deftest user-handler
  (testing "400 OK"
    (is (= {}))))

(deftest authenticate-user-handler
  (testing "400 OK"))

(deftest change-user-password-handler
  (testing "400 OK"))
