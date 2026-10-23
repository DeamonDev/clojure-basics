(ns first-steps.core
  (:require [clojure.string :as str]))

(defn greet
  "Returns a greeting for `name`. Blank or nil names get a generic greeting."
  [name]
  (if (str/blank? name)
    "Hello, stranger!"
    (str "Hello, " name "!")))

(defn fizzbuzz
  "Returns the FizzBuzz word for `n`."
  [n]
  (cond
    (zero? (mod n 15)) "FizzBuzz"
    (zero? (mod n 3))  "Fizz"
    (zero? (mod n 5))  "Buzz"
    :else              (str n)))

;; clojure -M -m first-steps.core Ada
(defn -main [& args]
  (println (greet (first args)))
  (run! (comp println fizzbuzz) (range 1 16)))

;; "Rich comment" block: never runs when the file is loaded, but you can
;; evaluate each form from your editor's REPL. Very common Clojure idiom.
(comment
  (greet "Ada")
  (greet nil)
  (map fizzbuzz (range 1 16))
  (-main "REPL"))
