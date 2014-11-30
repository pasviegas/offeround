(ns offeround-api.endpoints.v1-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [offeround-api.support.templates :as templates]
            [offeround-api.endpoints.app :refer :all]
            [offeround-api.helpers :refer :all]
            [cheshire.core :refer :all]))

(deftest test-v1
  
  (testing "v1 routes "    
    
    (testing "/ token route"
      
      ;
      ; curl -XPOST http://127.0.0.1:3000/v1/token -I
      ;
      ; HTTP/1.1 401 Unauthorized
      ; Access-Control-Allow-Headers: Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization
      ; Access-Control-Allow-Origin: *
      ; Content-Type: application/json
      ; Www-Authenticate: Basic realm="Authorization Required"
      ; Content-Length: 0
      ;
      (let [response (handler (request :post "/token"))]
        (is (= (:status response) 401))
        (is (= ((:headers response) "Access-Control-Allow-Headers") "Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization"))
        (is (= ((:headers response) "Access-Control-Allow-Origin") "*"))
        (is (= ((:headers response) "Www-Authenticate:") "Basic realm=\"Authorization Required\""))
        (is (= ((:headers response) "Content-Type") "application/json"))
        (is (empty? (:body response))))

      ;
      ; curl -XPOST -u locafox:LocaF#xes! http://127.0.0.1:3000/v1/token -I
      ;
      ; HTTP/1.1 200 OK
      ; Access-Control-Allow-Headers: Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization
      ; Access-Control-Allow-Origin: *
      ; Content-Type: application/json
      ; Content-Length: 53
      ;    
      ; {"token":"9akgrTI5EzdrPcq6Ez0mM9uTcZAAvkRLe1TuAyNC"}
      ;  
      (let [response (handler (request-with-auth :post "/token"))]
        (is (= (:status response) 200))
        (is (= ((:headers response) "Access-Control-Allow-Headers") "Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization"))
        (is (= ((:headers response) "Access-Control-Allow-Origin") "*"))
        (is (= ((:headers response) "Content-Type") "application/json"))
        (is (contains? (parse-string (:body response) true) :token))))
    
    
    (testing "/ offers route"
      
      ;
      ; curl -XPOST http://127.0.0.1:3000/v1/offers -I
      ;
      ; HTTP/1.1 401 Unauthorized
      ; Access-Control-Allow-Headers: Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization
      ; Access-Control-Allow-Origin: *
      ; Content-Type: application/json
      ; Www-Authenticate: Basic realm="Authorization Required"
      ; Content-Length: 0
      ;
      (let [response (handler (request :post "/offers"))]
        (is (= (:status response) 401))
        (is (= ((:headers response) "Access-Control-Allow-Headers") "Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization"))
        (is (= ((:headers response) "Access-Control-Allow-Origin") "*"))
        (is (= ((:headers response) "Www-Authenticate:") "Basic realm=\"Authorization Required\""))
        (is (= ((:headers response) "Content-Type") "application/json"))
        (is (empty? (:body response))))
      
      ;
      ; curl -XPOST -u locafox:LocaF#xes! http://127.0.0.1:3000/v1/offers -I
      ;
      ; HTTP/1.1 401 Unauthorized
      ; Access-Control-Allow-Headers: Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization
      ; Access-Control-Allow-Origin: *
      ; Content-Type: application/json
      ; Content-Length: 26
      ;      
      ; {"status":"unauthorized"}
      ;
      (let [response (handler (request-with-auth :post "/offers"))]
        (is (= (:status response) 401))
        (is (= ((:headers response) "Access-Control-Allow-Headers") "Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization"))
        (is (= ((:headers response) "Access-Control-Allow-Origin") "*"))
        (is (= ((:headers response) "Content-Type") "application/json"))
        (is (= (:body response) (templates/unauthorized))))
      
      ;
      ;
      ; curl -XPOST -u locafox:LocaF#xes! http://127.0.0.1:3000/v1/offers?token=9akgrTI5EzdrPcq6Ez0mM9uTcZAAvkRLe1TuAyNC -I
      ;
      ; HTTP/1.1 200 OK
      ; Access-Control-Allow-Headers: Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization
      ; Access-Control-Allow-Origin: *
      ; Content-Type: application/json
      ; Content-Length: ....
      ;
      ; [{id: 1, lat: 52.47, long: 13.47},{.........
      ;      
      (let [response (handler (request-with-auth-and-token :post "/offers"))]
        (is (= (:status response) 200))
        (is (= ((:headers response) "Access-Control-Allow-Headers") "Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization"))
        (is (= ((:headers response) "Access-Control-Allow-Origin") "*"))
        (is (= ((:headers response) "Content-Type") "application/json"))
        (is (< 0 (count (parse-string (:body response)))))))))
