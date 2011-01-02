;; -*- clojure -*-
;; Copyright (c) 2010 Pierre-Charles David.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.
(ns haystack
  (:use [haystack commits subversion reporting]
        [clojure.contrib.duck-streams :only [file-str make-parents]])
  (:require [clj-json [core :as json]])
  (:gen-class))

;; The default values for optional configuration parameters.
(def default-configuration {:output-dir "result"})

(defn make-path
  ([]
     ".")
  ([& segments]
     (when (seq segments)
       (apply file-str (interleave segments (repeat java.io.File/separator))))))

(defn spit-report [f contents]
  (make-parents f)
  (spit f contents))

(defn haystack-report [project]
  (let [commits (load-commits (slurp (:history-source project)))
	impact (ticket->files commits)
	reverse-impact (file->tickets commits)
	output (:output-dir project)]
    (spit-report (make-path output "report.html") (report-impact impact project))
    (spit-report (make-path output "reverse-report.html") (report-reverse-impact reverse-impact project))))

(defn -main [& args]
  (if (= 1 (count args))
    (let [string-config (json/parse-string (slurp (nth args 0)))
	  config (apply hash-map (flatten (for [[k v] string-config] [(keyword k) v])))]
      (haystack-report (merge default-configuration config)))
    (println "Usage: <haystack-cmd> config.json")))
