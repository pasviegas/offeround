(ns offeround-api.endpoints.v1
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :refer :all]
            [offeround-api.endpoints.not-found :as not-found]
            [offeround-api.support.templates :as templates]
            [offeround-api.support.wrappers :as wrappers]
            [offeround-api.support.token :as token]
            [offeround-api.data.offer :as offer]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Helpers
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def token (atom nil))

(defn authenticated? [name pass]
  (and (= name "locafox")
       (= pass "LocaF#xes!")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Handlers
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn offers-handler 
  "POST /v1/offers handler - returns a json array of offers"
  []
  {:status 200
   :body (generate-string (offer/get-all))})

(defn token-handler 
  "POST /v1/token handler - creates and returns a json containing the token"
  []
  (swap! token (fn [old] (token/create)))
  {:status 200
   :body (generate-string {:token @token})})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Offers Routes
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defroutes offers-routes*
  (POST    "/v1/offers"  [] (offers-handler)))

(def offers-routes 
  "Offers routes wrapped in their specific middleware"
  (-> offers-routes*
      (wrappers/token-authentication token)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; V1 Api Routes
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defroutes v1-routes*
  (POST    "/v1/token"  [] (token-handler))
  (POST    "/v1/offers" [] offers-routes))

(def v1-routes 
  "All v1 routes wrapped in their specific middleware"
  (-> v1-routes*
      (wrappers/basic-authentication authenticated?)
      (wrappers/cross-origin-headers)
      (wrappers/content-type "application/json")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Complete Api Routes
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defroutes api-routes
  (POST "/v1/token"  [] v1-routes)
  (POST "/v1/offers" [] v1-routes)
  (route/not-found not-found/not-found-routes))

