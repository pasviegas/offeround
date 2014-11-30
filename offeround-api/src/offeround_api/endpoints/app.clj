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
; Basic routes
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defroutes basic-routes*
  (OPTIONS  "*" [] "")
  (GET      "/" [] (root-handler)))

(def basic-routes
  "All basic routes wrapped in their specific middleware"
  (-> basic-routes*
      (wrappers/cross-origin-headers)
      (wrappers/content-type "application/json")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Complete server routes
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defroutes app-routes
  (OPTIONS  "*"      [] basic-routes)
  (GET      "/"      [] basic-routes)
  (POST     "/v1/*"  [] v1/api-routes)
  (route/not-found not-found/not-found-routes))

(def handler app-routes)
