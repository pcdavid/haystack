(ns haystack.www
  (:use [haystack config commits subversion reporting])
  (:use compojure.core)
  (:require [ring.adapter.jetty :as jetty])
  (:require [compojure.route :as route])
  (:gen-class))

(def project (atom {}))

(defroutes report-server
  (GET "/impact" [] (report-impact (:impact @project) (:config @project)))
  (GET "/reverse-impact" [] (report-reverse-impact (:reverse-impact @project) (:config @project)))
  (route/not-found "Page not found"))

(defn -main [& args]
  (if (= 1 (count args))
    (let [config (load-configuration (nth args 0))
	  commits (load-commits (slurp (:history-source config)))]
      (reset! project {:config config
		       :commits commits
		       :impact (ticket->files commits)
		       :reverse-impact (file->tickets commits)})
      (jetty/run-jetty #'report-server {:port 8080}))
    (println "Usage: <haystack-cmd> config.json")))
