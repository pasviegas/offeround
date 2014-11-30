(ns offeround-api.support.token-test
  (:require [offeround-api.support.token :as token]
            [clojure.test :refer :all]))

(deftest test-token
  
  (testing "root route"
    (let [token (token/create)]
      (is (= 40 (count token))))))
