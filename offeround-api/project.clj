(defproject offeround-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.2.0"]
                 [cheshire "5.3.1"]
                 [org.clojure/data.codec "0.1.0"]
                 [ring-basic-authentication "1.0.5"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler offeround-api.endpoints.app/handler}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
