; -*- clojure -*-
; Copyright (c) 2010 Pierre-Charles David.
; The use and distribution terms for this software are covered by the
; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
; which can be found in the file epl-v10.html at the root of this distribution.
; By using this software in any fashion, you are agreeing to be bound by
; the terms of this license.
; You must not remove this notice, or any other, from this software.

;; Haystack reads the commit history from a project's SCM, and
;; produces HTML reports which links issues from a bug tracker with
;; the files which were modified to fix the issue.

;; It can prove very useful when you need to come back to some feature
;; to get a quick glance at which parts of the source code where
;; impacted. If a regression is feature #123, the haystack report will
;; allow you to quickly narrow down the parts of the code to focus on.

;; Haystack is a reimplementation from scratch of a script I wrote in
;; Ruby for internal use at [Obeo](http://www.obeo.fr/). Currently it
;; only support Subversion log files in XML format as input, and
;; assumes tickets are referenced in the commit messages using a
;; Trac-like syntax, e.g. "closes ticket #1234 - short description".
;; Hopefully this will be fixed in future releases.

;; ### Note
;;
;; My main goal is to learn Clojure and its ecosystem on a simple but
;; non-trivial problem, so the roadmap for future versions will
;; probably be based on cool ideas and libraries I want to play with
;; more than on real utility.

;; # Command-line support
(ns haystack
  "A version of haystack which can be invoked from the command-line."
  (:use [haystack config commits subversion reporting]
	[clojure.java.io :only [make-parents]]
	[clojure.contrib.json :only [read-json]]
        [clojure.contrib.duck-streams :only [file-str]])
  (:gen-class))

;; Some filesystem utilities first.

(defn make-path
  "Creates a well-formed filesystem path from a list of segments."
  ([]
     ".")
  ([& segments]
     (when (seq segments)
       (apply file-str (interleave segments (repeat java.io.File/separator))))))

(defn spit-report [f contents]
  (make-parents f)
  (spit f contents))

;; The main work is done here: we parse the commit history, compute
;; the cross-reference information, and generate the static HTML
;; reports from that.
(defn haystack-report [project]
  (let [commits (load-commits (slurp (:history-source project)))
	impact (ticket->files commits)
	reverse-impact (file->tickets commits)
	output (:output-dir project)]
    (spit-report (make-path output "report.html") (report-impact impact project))
    (spit-report (make-path output "reverse-report.html") (report-reverse-impact reverse-impact project))))

(defn -main [& args]
  (if (= 1 (count args))
      (haystack-report (load-configuration (nth args 0)))
    (println "Usage: <haystack-cmd> config.json")))
