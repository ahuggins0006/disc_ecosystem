(ns disc-message-db.core
  (:require [asami.core :as d]
            [asami-loom.index :as lidx]
            [asami-loom.label :as label]
            [loom.io :as loom-io]
            )
  (:gen-class))


;; Create an in-memory database, named dbname
;;(def db-uri "asami:mem://dbname")
(def db-uri "asami:mem://disc-eco")
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
       :where [?m :movie/title ?title]
              [?m :movie/release-year ?year]
              [?m :movie/genre ?genre]
              [(> ?year 1990)]
       ] db)

;; triples

(def work-tree [[:A :title "CEO"]
                [:B :title "CTO"]
                [:C :title "Engineering Manager"]
                [:D :title "QA Engineer"]
                [:E :title "Engineer"]])

@(d/transact conn {:tx-triples work-tree})


;; try a concrete example

(def publishers [[:layout-controller :publishes "ServiceStatus"]
                 [:layout-controller :publishes "DiscNotification"]
                 [:layout-controller :publishes "DiscCommandAck"]
                 [:layout-controller :publishes "UpdateLayoutConfiguration"]
                 [:layout-controller :publishes "LayoutConfiguration"]
                 [:layout-controller :publishes "LayoutConfigurationResponse"]

                 [:disc-status-monitor :publishes "InactiveNode"]
                 [:disc-status-monitor :publishes "LostNode"]
                 [:disc-status-monitor :publishes "InactiveService"]
                 [:disc-status-monitor :publishes "LostService"]
                 [:disc-status-monitor :publishes "ServiceStatus"]
                 [:disc-status-monitor :publishes "DiscNotification"]
                 [:disc-status-monitor :publishes "DiscCommandAck"]

                 [:disc-frontend-bridge :publishes "DiscCommand"]
                 [:disc-frontend-bridge :publishes "RestartCluster"]
                 [:disc-frontend-bridge :publishes "ShutdownCluster"]
                 [:disc-frontend-bridge :publishes "StartCluster"]
                 [:disc-frontend-bridge :publishes "UpdateClusterConfiguration"]
                 [:disc-frontend-bridge :publishes "ServiceStatus"]
                 [:disc-frontend-bridge :publishes "NodeStatus"]
                 [:disc-frontend-bridge :publishes "ClusterOperationalStatus"]
                 [:disc-frontend-bridge :publishes "ClusterConfiguration"]
                 [:disc-frontend-bridge :publishes "Notification"]
                 [:disc-frontend-bridge :publishes "ApplicationStatus"]
                 [:disc-frontend-bridge :publishes "LayoutConfigurationRequest"]
                 [:disc-frontend-bridge :publishes "UpdateLayoutConfiguration"]
                 [:disc-frontend-bridge :publishes "DiscNotification"]
                 [:disc-frontend-bridge :publishes "DiscCommandAck"]
                 [:disc-frontend-bridge :publishes "BridgeStatus"]
                 [:disc-frontend-bridge :publishes "LayoutConfiguration"]
                 [:disc-frontend-bridge :publishes "RequestClusterConfiguration"]

                 [:cluster-controller :publishes "ClusterStatus"]
                 [:cluster-controller :publishes "ClusterConfiguration"]
                 [:cluster-controller :publishes "SetClusterConfiguration"]
                 [:cluster-controller :publishes "DiscCommandAck"]
                 [:cluster-controller :publishes "DiscNotification"]
                 [:cluster-controller :publishes "ServiceStatus"]
                 [:cluster-controller :publishes "DiscCommand"]
                 [:cluster-controller :publishes "ShutdownApplication"]
                 [:cluster-controller :publishes "LaunchApplication"]
                 [:cluster-controller :publishes "LaunchService"]])

(def handlers [[:layout-controller :handles "ServiceStatus"]
               [:layout-controller :handles "DiscCommand"]
               [:layout-controller :handles "UpdateLayoutConfiguration"]
               [:layout-controller :handles "LayoutConfigurationRequest"]

               [:disc-status-monitor :handles "ServiceStatus"]
               [:disc-status-monitor :handles "DiscCommand"]
               [:disc-status-monitor :handles "NodeStatus"]

               [:disc-frontend-bridge :handles "ServiceStatus"]
               [:disc-frontend-bridge :handles "DiscCommand"]
               [:disc-frontend-bridge :handles "DiscCommandAck"]
               [:disc-frontend-bridge :handles "NodeStatus"]
               [:disc-frontend-bridge :handles "DiscNotification"]
               [:disc-frontend-bridge :handles "ClusterStatus"]
               [:disc-frontend-bridge :handles "ClusterConfiguration"]
               [:disc-frontend-bridge :handles "InactiveNode"]
               [:disc-frontend-bridge :handles "InactiveService"]
               [:disc-frontend-bridge :handles "LostNode"]
               [:disc-frontend-bridge :handles "LostService"]
               [:disc-frontend-bridge :handles "LayoutConfiguration"]
               [:disc-frontend-bridge :handles "ApplicationStatus"]
               [:disc-frontend-bridge :handles "LayoutConfigurationResponse"]
               [:disc-frontend-bridge :handles "RequestNodeAction"]
               [:disc-frontend-bridge :handles "RequestServiceAction"]
               [:disc-frontend-bridge :handles "SetNotificationLevel"]
               [:disc-frontend-bridge :handles "RequestDataUpdate"]
               [:disc-frontend-bridge :handles "StartCluster"]
               [:disc-frontend-bridge :handles "ShutdownCluster"]
               [:disc-frontend-bridge :handles "RestartCluster"]
               [:disc-frontend-bridge :handles "UpdateClusterConfiguration"]
               [:disc-frontend-bridge :handles "LayoutConfiguration"]
               [:disc-frontend-bridge :handles "DiscCommand"]
               [:disc-frontend-bridge :handles "DiscCommandAck"]
               [:disc-frontend-bridge :handles "NodeStatus"]
               [:disc-frontend-bridge :handles "DiscNotification"]
               [:disc-frontend-bridge :handles "ClusterStatus"]
               [:disc-frontend-bridge :handles "ClusterConfiguration"]
               [:disc-frontend-bridge :handles "InactiveNode"]
               [:disc-frontend-bridge :handles "InactiveService"]
               [:disc-frontend-bridge :handles "LostNode"]
               [:disc-frontend-bridge :handles "LostService"]
               [:disc-frontend-bridge :handles "LayoutConfiguration"]
               [:disc-frontend-bridge :handles "ApplicationStatus"]
               [:disc-frontend-bridge :handles "LayoutConfigurationResponse"]
               [:disc-frontend-bridge :handles "RequestNodeAction"]
               [:disc-frontend-bridge :handles "RequestServiceAction"]
               [:disc-frontend-bridge :handles "SetNotificationLevel"]
               [:disc-frontend-bridge :handles "RequestDataUpdate"]
               [:disc-frontend-bridge :handles "StartCluster"]
               [:disc-frontend-bridge :handles "ShutdownCluster"]
               [:disc-frontend-bridge :handles "RestartCluster"]
               [:disc-frontend-bridge :handles "UpdateClusterConfiguration"]
               [:disc-frontend-bridge :handles "LayoutConfiguration"]

               [:cluster-controller :handles "ServiceStatus"]
               [:cluster-controller :handles "ApplicationStatus"]
               [:cluster-controller :handles "DiscCommand"]
               [:cluster-controller :handles "DiscCommandAck"]
               [:cluster-controller :handles "NodeStatus"]
               [:cluster-controller :handles "SetClusterConfiguration"]
               [:cluster-controller :handles "UpdateClusterConfiguration"]
               [:cluster-controller :handles "RequestClusterConfiguration"]
               [:cluster-controller :handles "StartCluster"]
               [:cluster-controller :handles "RestartCluster"]
               [:cluster-controller :handles "ShutdownCluster"]
               [:cluster-controller :handles "LaunchServiceResponse"]
               [:cluster-controller :handles "LaunchApplicationResponse"]
               [:cluster-controller :handles "ShutdownApplicationResponse"]
               [:cluster-controller :handles "LostService"]])



@(d/transact conn {:tx-triples publishers})
@(d/transact conn {:tx-triples handlers})

;; find services who publish ServiceStatus

(d/q '[:find [?m ...]
     :where [?m :publishes "ServiceStatus"]] db)
;; => (:layout-controller :disc-status-monitor :disc-frontend-bridge :cluster-controller)

;; find which messages disc status monitor publishes

(d/q '[:find [?m ...]
       :where [:disc-status-monitor :publishes ?m]] db)
;; => ("InactiveNode" "LostNode" "InactiveService" "LostService" "ServiceStatus" "DiscNotification" "DiscCommandAck")

;; cluster controller
(d/q '[:find [?m ...]
       :where [:cluster-controller :publishes ?m]] db)
;; => ("DiscCommand" "SetClusterConfiguration" "DiscNotification" "ShutdownApplication" "LaunchApplication" "DiscCommandAck" "ServiceStatus" "LaunchService" "ClusterStatus" "ClusterConfiguration")

;; find which messages both disc status monitor and layout controller publish

(d/q '[:find [?m ...]
       :where [:disc-status-monitor :publishes ?m]
              [:layout-controller :publishes ?m]

       ] db)
;; => ("ServiceStatus" "DiscNotification" "DiscCommandAck")


;; find services who handle ServiceStatus

(d/q '[:find [?m ...]
       :where [?m :handles "ServiceStatus"]] db)
;; => (:layout-controller :disc-status-monitor :disc-frontend-bridge :cluster-controller)

;; find what messages layout controller uses

(d/q '[:find [?m ...]
       :where [:layout-controller _ ?m]] db)
;; => ("ServiceStatus" "DiscNotification" "DiscCommandAck" "UpdateLayoutConfiguration" "LayoutConfiguration" "LayoutConfigurationResponse" "DiscCommand" "LayoutConfigurationRequest")

;; find all messages
       :where [_ _ ?m]
(count (d/q '[:find [?m ...]
              :where [_ _ ?m]
              ] db))


;; use loom to generate graph

(def graph (d/graph (d/db conn)))

(defn edge-label
  [g s d]
  (str (d/q '[:find ?edge . :in $ ?a ?b :where (or [?a ?edge ?b] [?b ?edge ?a])] g s d)))

(defn node-label
  [g n]
  (let [id (d/q [:find '?id '. :where [n :db/ident '?id]] g)]
    (cond id (str id)
          (and (keyword? n) (= (namespace n) "tg")) (str ":" (name n))
          :default (str n))))



(loom-io/view graph :fmt :png :alg :sfdp :edge-label (partial edge-label graph) :node-label (partial node-label graph))

(loom-io/dot graph "resources/graph.gv")
(loom-io/dot graph "resources/graph2.gv")
(loom-io/view graph)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
;; => #'disc-message-db.core/-main

