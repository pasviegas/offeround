(ns offeround-api.endpoints.not-found
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [offeround-api.support.wrappers :as wrappers]
            [offeround-api.support.templates :as templates]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Not found route
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defroutes not-found-routes*
  (route/not-found (templates/not-found)))

(def not-found-routes
  "All not found routes wrapped in their specific middleware"
  (-> not-found-routes*
      (wrappers/cross-origin-headers)
      (wrappers/content-type "text/plain")))