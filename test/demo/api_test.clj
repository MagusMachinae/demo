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
  (testing "valid GET request to /api/authenticate returns 200 OK and user"
    (let  [res (api/authenticate-user-handler test-db
                (mock/request :get "/api/user?id=i-exist&password=for-now"))]
      (is (= 200
             (:status res)))
      (is (= {:id       "i-exist"
              :role :user}
             (:result (:body res))))))
  (testing "invalid GET request to /api/authenticate throws 400")
    (let [res (api/authenticate-user-handler test-db
               (mock/request :get "/api/user?id=i-don't-exist&password=for-now"))]
      (is (= 400
             (:status res)))))

(deftest change-user-password-handler
  (testing "400 OK"))
