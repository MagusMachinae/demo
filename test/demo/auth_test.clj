(ns demo.api-test
  (:require [clojure.test :refer [deftest is testing]]
            [demo.backend.auth :as auth]))

(def test-db (atom {"i-exist" {:id       "i-exist"
                                         :password "correct"
                                         :role     :user}}))

(defmacro catch-thrown-info [f]
   `(try
      ~f
      (catch
       clojure.lang.ExceptionInfo e#
        {:msg (ex-message e#) :data (ex-data e#)})))


(deftest password-rules
  (testing "all rules are applied individually and in composition"
    (is (= #{:password.error/too-short}
           (auth/password-rules "ABa$1")))
    (is (= #{:password.error/missing-special-character}
           (auth/password-rules "ABacdasw1")))
    (is (= #{:password.error/missing-lowercase}
           (auth/password-rules "ABCD£!£$%")))
    (is (= #{:password.error/missing-uppercase}
           (auth/password-rules "abcd££%$£")))
    (is (= #{:password.error/too-short
             :password.error/missing-uppercase
             :password.error/missing-lowercase
             :password.error/missing-special-character}
           (auth/password-rules "")))))

(deftest authenticate-user
  (testing "exceptions are thrown when user or password checks return nil"
    (is (= {:msg "Invalid username or password",
            :data {:reason :login.error/invalid-credentials}}
          (catch-thrown-info (auth/authenticate-user test-db "i-exist" "incorrect"))
          (catch-thrown-info (auth/authenticate-user test-db "i-don't-exist" "")))))
  (testing "successful login returns user map without password"
    (is (= {:id "i-exist"
            :role user}
           (auth/authenticate-user test-db "i-exist" "correct")))))

(deftest change-user-password
   (testing "trying to reset the password of a user who doesn't exist throws an exception"
     (is (= {:msg  "User does not exist"
             :data {:reason :change-user-password.error/does-not-exist}}
            (auth/change-user-password test-db "i-don't-exist" "new-pass"))))
  (testing "db is updated with new password"
    (let [test-db-2 test-db
          _         (auth/change-user-password test-db-2 "i-exist" "new-pass")]
      (is (= "new-pass"
             (get-in @test-db-2 ["i-exist" :password]))))))

(deftest create-user)
