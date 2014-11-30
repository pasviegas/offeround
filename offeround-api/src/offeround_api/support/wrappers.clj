(ns offeround-api.support.wrappers
  (require [offeround-api.support.templates :as templates]
           [ring.middleware.basic-authentication :refer [wrap-basic-authentication]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Header middleware
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn wrap-header 
  "Wraps the response with the appropriate header"
  [handler header value]
  (fn [request]
    (let [response (handler request)]
      (assoc-in response [:headers header] value))))

(defn content-type 
  "Wraps the response with the appropriate content-type"
  [handler content-type]
  (wrap-header handler "Content-Type", content-type))

(defn allow-origin 
  "Wraps the response with Access-Control-Allow-Origin header"
  [handler origin]
  (wrap-header handler "Access-Control-Allow-Origin", origin))

(defn allow-headers 
  "Wraps the response with Access-Control-Allow-Headers header"
  [handler headers]
  (wrap-header handler "Access-Control-Allow-Headers", headers))

(defn cross-origin-headers 
  "Wraps the response with the default Access-Control header"
  [handler] 
  (-> handler
      (allow-origin "*")
      (allow-headers "Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Authentication middleware
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn basic-authentication 
  "Given a predicate, authenticates the user or returns the appropriate response error"
  [handler predicate]
  (fn [request]
    (let [wrapped-handler (wrap-basic-authentication handler predicate)
          response (wrapped-handler request)]
      (cond (= (:status response) 200) response
            (= (:status response) 403) (assoc-in response [:status] 401)
            :else (-> response
                      (assoc-in  [:headers "Www-Authenticate:"] "Basic realm=\"Authorization Required\"")
                      (update-in [:headers] #(dissoc % "WWW-Authenticate"))
                      (assoc-in  [:body] ""))))))

(defn parse-query-string 
  "Parses query-string if exists"
  [qs]
  (if (> (count qs) 0) 
    (apply hash-map (clojure.string/split qs #"[&=]"))))

(defn token-unauthorized []
  {:status 403 :body (templates/unauthorized)})

(defn token-authentication 
  "Given a token, checks the existance of the same token in the query-string and returns accordingly"
  [handler token]
  (fn [request]
    (let [token @token
          query-string (parse-query-string (:query-string request))]
      (cond 
        (nil? token) (token-unauthorized)
        (= (get query-string "token") token) (handler request)
        :else (token-unauthorized)))))
