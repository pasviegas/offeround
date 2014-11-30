(ns offeround-api.endpoints.not-found-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [offeround-api.support.templates :as templates]
            [offeround-api.endpoints.app :refer :all]))

(deftest test-not-found
  
  (testing "not-found route"
    
    ;
    ; curl -XGET http://127.0.0.1:3000/invalid -I
    ;
    ; HTTP/1.1 404 Not Found
    ; Access-Control-Allow-Headers: Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization
    ; Access-Control-Allow-Origin: *
    ; Content-Type: text/plain
    ; Content-Length: 18
    ;
    ; 404 page not found
    ;
    (let [response (handler (mock/request :get "/invalid"))]
      (is (= (:status response) 404))
      (is (= ((:headers response) "Access-Control-Allow-Headers") "Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization"))
      (is (= ((:headers response) "Access-Control-Allow-Origin") "*"))
      (is (= ((:headers response) "Content-Type") "text/plain"))
      (is (= (:body response) (templates/not-found))))
    
    ;
    ; curl -XPOST http://127.0.0.1:3000/invalid -I
    ;
    ; HTTP/1.1 404 Not Found
    ; Access-Control-Allow-Headers: Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization
    ; Access-Control-Allow-Origin: *
    ; Content-Type: text/plain
    ; Content-Length: 18
    ;
    ; 404 page not found
    ;    
    (let [response (handler (mock/request :post "/invalid"))]
      (is (= (:status response) 404))
      (is (= ((:headers response) "Access-Control-Allow-Headers") "Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization"))
      (is (= ((:headers response) "Access-Control-Allow-Origin") "*"))
      (is (= ((:headers response) "Content-Type") "text/plain"))
      (is (= (:body response) (templates/not-found))))

    ;
    ; curl -XOPTIONS http://127.0.0.1:3000/invalid -I
    ;
    ; HTTP/1.1 200 OK
    ; Access-Control-Allow-Headers: Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization
    ; Access-Control-Allow-Origin: *
    ; Content-Type: application/json
    ; Content-Length: 0
    ;        
    (let [response (handler (mock/request :options "/invalid"))]
      (is (= (:status response) 200))
      (is (= ((:headers response) "Access-Control-Allow-Headers") "Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization"))
      (is (= ((:headers response) "Access-Control-Allow-Origin") "*"))
      (is (= ((:headers response) "Content-Type") "application/json"))
      (is (= (:body response) "")))))
