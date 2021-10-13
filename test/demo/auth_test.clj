(ns demo.api-test
  (:require [clojure.test :refer [deftest is testing]]
            [demo.backend.auth :as auth]))


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
