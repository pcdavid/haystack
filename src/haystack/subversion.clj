; -*- clojure -*-
; Copyright (c) 2010 Pierre-Charles David.
; The use and distribution terms for this software are covered by the
; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
; which can be found in the file epl-v10.html at the root of this distribution.
; By using this software in any fashion, you are agreeing to be bound by
; the terms of this license.
; You must not remove this notice, or any other, from this software.
(ns haystack.subversion
  (:require [haystack.commits :as commits]
            [clojure.xml :as xml]))

;; Generic XML helpers
(defn- tag= [name]
  "Returns a predicate matching an XML element with the specified tag
name."
  (fn [elt] (= (xml/tag elt) name)))

(defn- text-content [elt]
  "Returns the textual content of an XML element."
  (if elt
    (first (xml/content elt))
    nil))

(defn children [name elt]
  "Returns the childen of the XML element 'elt' with tag name 'name'."
  (filter (tag= name) (xml/content elt)))

(defn child [name elt]
  "Returns the first child of the XML element 'elt' with tag name 'name'."
  (first (children name elt)))

;; Extract information from parsed Subversion XML logs
(defn entry-revision [entry]
  "Extracts the revision number (an int) from an XML logentry."
  (Integer/valueOf (:revision (xml/attrs entry))))

(defn entry-author [entry]
  "Extracts the author name from an XML logentry."
  (text-content (child :author entry)))

(defn entry-message [entry]
  "Extracts the commit message from an XML logentry. Returns nil if
there is no message."
  (text-content (child :msg entry)))

(def ticket-re #"# *([0-9]+)")

(defn entry-tickets [entry]
  "Returns the names of all the tickets mentioned in the first line of
the entry's message."
  (if-let [msg (entry-message entry)]
    (let [line1 (first (.split msg "\n"))]
      (map #(Integer/valueOf (second %)) (re-seq ticket-re line1)))))

(defn entry-paths [entry]
  "Extracts the paths of the files referenced by an XML logentry, as a
set of strings."
    (set (map text-content (->> entry (child :paths) (children :path)))))

(defn make-commit [entry]
  "Creates a commit struct from the information in an XML logentry."
  (struct commits/commit
          (entry-revision entry)
          (entry-tickets entry)
          (entry-author entry)
          (entry-message entry)
          (entry-paths entry)))

(defn- parse-commits [src]
  (let [entries (xml/parse src)]
    (map make-commit (children :logentry entries))))

(defn load-commits
  ([]
     (parse-commits System/in))
  ([s]
     (parse-commits (org.xml.sax.InputSource. (java.io.StringReader. s)))))
