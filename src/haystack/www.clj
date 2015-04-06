; -*- clojure -*-
; Copyright (c) 2011 Pierre-Charles David.
; The use and distribution terms for this software are covered by the
; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
; which can be found in the file epl-v10.html at the root of this distribution.
; By using this software in any fashion, you are agreeing to be bound by
; the terms of this license.
; You must not remove this notice, or any other, from this software.
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
