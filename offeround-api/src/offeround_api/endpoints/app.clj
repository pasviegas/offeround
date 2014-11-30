(ns offeround-api.endpoints.app
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [offeround-api.endpoints.v1 :as v1]
            [offeround-api.endpoints.not-found :as not-found]
            [offeround-api.support.templates :as templates]
            [offeround-api.support.wrappers :as wrappers]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Handler
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn root-handler 
  "GET / handler - returns {status:OK}"
  []
  {:status 200
   :body (templates/ok)})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Root Route
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defroutes root-routes*
  (OPTIONS  "*" [] "")
  (GET  	  "/" [] (root-handler)))

(def root-routes
  "All root routes wrapped in their specific middleware"
  (-> root-routes*
      (wrappers/cross-origin-headers)
      (wrappers/content-type "application/json")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Basic Routes
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defroutes basic-routes
  (OPTIONS  "*" [] root-routes)
  (GET  	  "/" [] root-routes)
  (route/not-found not-found/not-found-routes))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Complete server routes
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defroutes app-routes
  (OPTIONS  "*"  [] basic-routes)
  (GET  	  "*"  [] basic-routes)
  (POST  	  "*"  [] v1/api-routes))

(def handler app-routes)
