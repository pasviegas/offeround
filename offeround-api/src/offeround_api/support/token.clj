(ns offeround-api.support.token
  (:require [cheshire.core :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Token helpers
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn first-eight 
  "Get the firtst eight characters of a uuid"
  [data]
  (clojure.string/replace data #"(\w{8})(\w{24})" "$1"))

(defn uuid 
  "Creates a UUID string with no dashes"
  []
  (clojure.string/replace 
    (str (java.util.UUID/randomUUID))
    #"(\w{8})-(\w{4})-(\w{4})-(\w{4})-(\w{12})"
    "$1$2$3$4$5"))

(defn create 
  "Creates a random token with length of 40 characters"
  [] 
  (str (uuid) (first-eight (uuid))))
