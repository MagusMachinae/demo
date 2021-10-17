(ns demo.api-test
  (:require [clojure.test :refer [deftest is testing]]
            [demo.backend.api :as api]
            [ring.mock.request :as mock]))

(def valid-request {:id       "i-exist"
                    :password "for-now"})

(def invalid-reuest {:id       "i-don't-exist"
                     :password "do-i?"})

(def test-db (atom {"i-exist" {:id       "i-exist"
                               :password "for-now"}}))

(deftest authenticate-user-handler
  (testing "400 OK"))

(deftest change-user-password-handler
  (testing "400 OK"))
