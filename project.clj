(defproject impact-report "0.1.0-SNAPSHOT"
  :description "Generates an HTML report of Trac tickets vs Subversion files impacted."
  :dependencies [[org.clojure/clojure "1.1.0"]
		 [org.clojure/clojure-contrib "1.1.0"]
		 [clj-html "0.1.0"]]
  :dev-dependencies [[leiningen/lein-swank "1.1.0"]
		     [lein-run "1.0.0"]])
