(ns offeround-api.data.offer-test
  (:require [offeround-api.data.offer :refer :all]
            [clojure.test :refer :all]))

(deftest test-offer
  
  (testing "offer creation"
    (let [offer (to-offer 1)]
      (is (> (:id offer) 0))
      (is (> (:lat offer) 52.47))
      (is (< (:lat offer) 52.55))
      (is (> (:long offer) 13.24))
      (is (< (:long offer) 13.50)))))
