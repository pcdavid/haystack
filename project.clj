(defproject haystack "0.3.0-SNAPSHOT"
  :description "Cross-references SCM history with issue trackers to
  find out which source changes correspond to which issue/ticket, and
  vice versa."
  :dependencies [[org.clojure/clojure "1.2.0"]
		 [org.clojure/clojure-contrib "1.2.0"]
		 [ring/ring-core "0.3.5"]
		 [ring/ring-devel "0.3.5"]
		 [ring/ring-jetty-adapter "0.3.5"]
		 [compojure "0.5.3"]
		 [hiccup "0.3.1"]]
  :main haystack
  :dev-dependencies [[swank-clojure "1.2.1"]
		     [marginalia "0.5.0-alpha"]])
