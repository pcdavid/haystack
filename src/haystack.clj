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
  (:gen-class))

(defn spit-report [path contents]
  (let [f (file-str path)]
    (make-parents f)
    (spit f contents)))

(defn haystack-report [project]
  (let [commits (load-commits (slurp (:history-source project)))
	impact (ticket->files commits)
	reverse-impact (file->tickets commits)]
    (spit-report "result/report.html" (report-impact impact project))
    (spit-report "result/reverse-report.html" (report-reverse-impact reverse-impact project))))
  
(defn -main [& args]
  (if (= 3 (count args))
    (haystack-report (zipmap [:history-source :path-prefix :ticket-prefix] args))
    (println "Usage: <haystack-cmd> log-file path-prefix ticket-prefix")))
