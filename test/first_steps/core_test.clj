(ns first-steps.core-test
  (:require [clojure.test :refer [deftest is testing are]]
            [first-steps.core :as core]))

(deftest greet-test
  (testing "greets by name"
    (is (= "Hello, Ada!" (core/greet "Ada"))))
  (testing "falls back for blank names"
    (is (= "Hello, stranger!" (core/greet nil)))
    (is (= "Hello, stranger!" (core/greet "   ")))))

;; `are` is a table-driven test: the template is applied to each row.
(deftest fizzbuzz-test
  (are [n expected] (= expected (core/fizzbuzz n))
    1  "1"
    3  "Fizz"
    5  "Buzz"
    15 "FizzBuzz"
    16 "16"))
