;; -*- clojure -*-
;; Copyright (c) 2010 Pierre-Charles David.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.
(ns haystack.config
  (:use [clojure.contrib.json :only [read-json]]))

;; The default values for optional configuration parameters.
(def default-configuration {:output-dir "result"})

(defn load-configuration [json-file-path]
  (try
    (let [config (read-json (slurp json-file-path))]
      (merge default-configuration config))
    (catch Exception e
      default-configuration)))
  
