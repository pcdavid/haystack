;; -*- clojure -*-
;; Copyright (c) 2010 Pierre-Charles David.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.
(ns haystack.commits)

(defstruct commit :revision :tickets :author :message :paths)

(defn commit-complete? [commit]
  (and (not-empty (:tickets commit))
       (not-empty (:paths commit))))

(defn ticket->files [commits]
  (apply merge-with concat (for [c commits :when (commit-complete? c)]
                             (let [t (first (:tickets c))]
                               (zipmap (repeat t) (map vector (:paths c)))))))

(defn file->tickets [commits]
  (apply merge-with concat (for [c commits :when (commit-complete? c)]
                             (let [t (first (:tickets c))]
                               (zipmap (:paths c) (repeat [t]))))))
