(ns offeround-api.support.templates
  (:require [cheshire.core :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Response templates
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn ok []
  (str (generate-string {:status "OK"}) "\n"))

(defn not-found []
  "404 page not found")

(defn unauthorized []
  (str (generate-string {:status "unauthorized"}) "\n"))