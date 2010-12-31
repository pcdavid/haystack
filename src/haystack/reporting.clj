;; -*- clojure -*-
;; Copyright (c) 2010 Pierre-Charles David.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.
(ns haystack.reporting
  (:require [hiccup.core :as html]))

(defn report-ticket-link [ticket base-url]
  [:a {:href (str base-url ticket)} (str "#" ticket)])

(defn report-file-path [path base-path]
  [:code (.replaceFirst path base-path "")])

(defn report-ticket-impact [[ticket paths] project]
  [:tr
   [:td (report-ticket-link ticket (:ticket-prefix project)) ]
   [:td (interpose [:br] (map #(report-file-path % (:path-prefix project)) paths))]])

(defn report-impact [ticket2files project]
  (html/html
   [:html
    [:head [:title "Files modified by each ticket"] ]
    [:body
     [:h1 "Files modified by each ticket"]
     (vec (concat [:table {:border 1}]
                  [[:tr [:th "Ticket"] [:th "Files"]]]
                  (map #(report-ticket-impact % project) (sort ticket2files))))]]))

(defn report-file-reverse-impact [[path tickets] project]
  [:tr
   [:td (report-file-path path (:path-prefix project))]
   [:td (interpose [:span ", "] (map #(report-ticket-link % (:ticket-prefix project)) tickets))]])

(defn report-reverse-impact [file2tickets project]
  (html/html
   [:html
    [:head [:title "Tickets impacting each file"] ]
    [:body
     [:h2 "Tickets impacting each file"]
     (vec (concat [:table {:border 1}]
                  [[:tr [:th "File"] [:th "Tickets"]]]
                  (map #(report-file-reverse-impact % project) (sort file2tickets))))]]))
