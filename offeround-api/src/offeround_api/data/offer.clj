(ns offeround-api.data.offer)
  
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Offer model
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;  
  
(defn to-offer 
  "Given an id, creates an offer within Berlin"
  [id]
  {:id (+ 1 id)
   :lat (+ 52.47 (rand 0.08))
   :long (+ 13.24 (rand 0.26))})    
  
(defn get-all 
  "Returns a sample list of offers (5 to 12 offers)"
  []
  (let [num-offers (+ 5 (rand-int 7))
        offers (take num-offers (repeatedly #(rand-int 5)))]
    (map to-offer offers)))  