(ns disc-message-db.core
  (:require [asami.core :as d])
  (:gen-class))


;; Create an in-memory database, named dbname
(def db-uri "asami:mem://dbname")
(d/create-database db-uri)

;; Create a connection to the database
(def conn (d/connect db-uri))

;; Data can be loaded into a database either as objects, or "add" statements:
(def first-movies [{:movie/title "Explorers"
                    :movie/genre "adventure/comedy/family"
                    :movie/release-year 1985}
                   {:movie/title "Demolition Man"
                    :movie/genre "action/sci-fi/thriller"
                    :movie/release-year 1993}
                   {:movie/title "Johnny Mnemonic"
                    :movie/genre "cyber-punk/action"
                    :movie/release-year 1995}
                   {:movie/title "Toy Story"
                    :movie/genre "animation/adventure"
                    :movie/release-year 1995}])

@(d/transact conn {:tx-data first-movies})

(def db (d/db conn))

(d/q '[:find ?movie-title
       :where [?m :movie/title ?movie-title]] db)
;; => (["Explorers"] ["Demolition Man"] ["Johnny Mnemonic"] ["Toy Story"])

(d/q '[:find ?title ?year ?genre
       :where [?m :movie/title ?title]])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

