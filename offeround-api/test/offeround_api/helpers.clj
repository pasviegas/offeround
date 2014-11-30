(ns offeround-api.helpers
  (:require [ring.mock.request :as mock]
            [clojure.data.codec.base64 :as base64]
            [cheshire.core :refer :all]
            [offeround-api.endpoints.app :refer :all]
            [clojure.test :refer :all]))

(defn- byte-transform [direction-fn string]
  (try
    (apply str (map char (direction-fn (.getBytes string))))
    (catch Exception _)))

(defn encode-base64 
  "Helper function to encode the authentication string"
  [string]
  (byte-transform base64/encode string))

(defn request 
  "Helper function to create the mocked api request"
  [method endpoint]
  (mock/request method (str "/v1" endpoint)))

(defn request-with-auth 
  "Helper function to create the mocked api request with authentication"
  [method endpoint]
  (mock/header 
    (request method endpoint) 
    "Authorization" 
    (str "Basic " (encode-base64 "locafox:LocaF#xes!"))))

(defn request-with-auth-and-token 
  "Helper function to create the mocked api request with authentication and required token"
  [method endpoint]
  (let [response (handler (request-with-auth :post "/token"))
        query-string-map (parse-string (:body response) true)]
    (mock/query-string (request-with-auth method endpoint) query-string-map)))
