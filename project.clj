(defproject haystack "0.1.0"
  :description "Cross-references SCM history with issue trackers to
  find out which source changes correspond to which issue/ticket, and
  vice versa."
  :dependencies [[org.clojure/clojure "1.2.0"]
		 [org.clojure/clojure-contrib "1.2.0"]
		 [hiccup "0.3.1"]]
  :dev-dependencies [[swank-clojure "1.2.1"]
		     [lein-run "1.0.0"]])
