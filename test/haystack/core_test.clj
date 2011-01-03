(ns haystack.core-test
  (:use [haystack commits subversion] :reload-all)
  (:use [clojure.test]))

(def single-commit "<?xml version='1.0'?>
<log>
<logentry revision='42'>
<author>guybrush</author>
<date>2010-06-23T20:05:56+0200</date>
<paths>
<path kind='' action='A'>/path/to/modified/file</path>
</paths>
<msg>the message</msg>
</logentry>
</log>
")

(deftest single-commit-no-ticket
  (let [commits (load-commits single-commit)]
    (is (= 1 (count commits)))
    (is (= "guybrush" (:author (first commits))))
    (is (= 42 (:revision (first commits))))
    (is (= 1 (count (:paths (first commits)))))
    (is (nil? (:ticket (first commits))))
    (is (not (commit-complete? (first commits))))))

(deftest commits
  (testing "Commits"
    (testing "consistency detection"
      (let [no-tickets (struct commit 42 [] "guybrush" "Fixed #1 and #2" ["src/foo.c" "src/foo.h"])
	    no-paths (struct commit 42 [1 2] "guybrush" "Fixed #1 and #2" [])
	    no-tickets-nor-paths (struct commit 42 [] "guybrush" "Fixed #1 and #2" [])
	    full  (struct commit 42 [1 2] "guybrush" "Fixed #1 and #2" ["src/foo.c" "src/foo.h"])]
	(is (not (commit-complete? no-tickets)))
	(is (not (commit-complete? no-paths)))
	(is (not (commit-complete? no-tickets-nor-paths)))
	(is (commit-complete? full))))))

