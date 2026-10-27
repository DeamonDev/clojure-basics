(ns first-steps.challenges
  "Fill-in-the-blank exercises. Replace every (todo) with real code until the
  matching test in test/first_steps/challenges_test.clj passes.

  Run one challenge:
    clojure -M:test -v first-steps.challenges-test/c01-square-test
  Run all of them:
    clojure -M:test -n first-steps.challenges-test"
  (:require [clojure.string :as str]))

(defn todo
  "Placeholder. If this gets called, the challenge isn't solved yet."
  []
  (throw (ex-info "TODO: not implemented yet" {})))

;; =============================================================================
;; Part 1 - Basics
;; =============================================================================

;; -----------------------------------------------------------------------------
;; 01. square
;; Return x multiplied by itself.
;;   (square 4) ;=> 16
;; Hint: everything is prefix notation: (* a b), not a * b.
(defn square [x]
  (* x x))

;; -----------------------------------------------------------------------------
;; 02. classify
;; Return :negative, :zero or :positive.
;;   (classify -3) ;=> :negative
;; Hint: `cond` takes test/expression pairs; `:else` is just a truthy keyword.
;;       See also neg?, zero?, pos?.
(defn classify [n]
  (cond
    (neg? n) :negative
    (zero? n) :zero
    :else :positive))

;; -----------------------------------------------------------------------------
;; 03. full-name
;; Build a full name from a person map. :middle-name is optional.
;;   (full-name {:first-name "Ada" :last-name "Lovelace"})
;;   ;=> "Ada Lovelace"
;;   (full-name {:first-name "John" :middle-name "Ronald" :last-name "Tolkien"})
;;   ;=> "John Ronald Tolkien"
;; Hint: destructure right in the arg vector:
;;         (defn full-name [{:keys [first-name middle-name last-name]}] ...)
;;       A missing key destructures to nil. Then: remove nil?, str/join.
(defn full-name
  [{:keys [first-name middle-name last-name]}]
  (str/join " " (remove nil? [first-name middle-name last-name])))

(defn full-name->>
  [{:keys [first-name middle-name last-name]}]
  (->> [first-name middle-name last-name]
       (remove nil?)
       (str/join " ")))

;; =============================================================================
;; Part 2 - Sequences
;; =============================================================================

;; -----------------------------------------------------------------------------
;; 04. sum-of-odd-squares
;; Sum the squares of the odd numbers in coll.
;;   (sum-of-odd-squares [1 2 3 4 5]) ;=> 35   ; 1 + 9 + 25
;; Hint: filter / map / reduce. Write it nested first, then rewrite it with
;;       the ->> threading macro and compare readability.
(defn sum-of-odd-squares [coll]
  (reduce + (map #(* % %) (filter odd? coll))))

(defn sum-of-odd-squares->> [coll]
  (->> coll
       (filter odd?)
       (map #(* % %))
       (reduce +)))

;; -----------------------------------------------------------------------------
;; 05. pairwise-diffs
;; Return the differences between consecutive elements.
;;   (pairwise-diffs [1 4 9 16]) ;=> (3 5 7)
;; Hint: `map` accepts several collections and stops at the shortest one:
;;       (map + [1 2 3] [10 20]) ;=> (11 22). What are xs and (rest xs)?
;;       Alternative: (partition 2 1 xs) + destructuring [[a b]].
(defn pairwise-diffs [xs]
  (map - (rest xs) xs))

;; -----------------------------------------------------------------------------
;; 06. word-frequencies
;; Count words case-insensitively, ignoring punctuation. Return {word count}.
;;   (word-frequencies "The cat. The hat!") ;=> {"the" 2, "cat" 1, "hat" 1}
;; Rule: build the map with `reduce`. Don't use `frequencies` (but do look at
;;       it afterwards).
;; Hint: str/lower-case, (re-seq #"\w+" s), (update m k f), and `fnil` to
;;       handle the "key not there yet" case.
(defn word-frequencies [text]
  (reduce (fn [counts word]
            (update counts word (fnil inc 0)))
          {}
          (re-seq #"\w+" (str/lower-case text))))

;; -----------------------------------------------------------------------------
;; 07. index-by
;; Build a map from (f item) to item.
;;   (index-by :id [{:id 1 :name "a"} {:id 2 :name "b"}])
;;   ;=> {1 {:id 1 :name "a"}, 2 {:id 2 :name "b"}}
;; Hint: keywords are functions: (:id m). Try `zipmap`, or `into {}` fed with
;;       [k v] vectors. Bonus: do it both ways.
(defn index-by [f coll]
  (zipmap (map f coll) coll))

;; -----------------------------------------------------------------------------
;; 08. my-reverse
;; Reverse a sequence using loop/recur. No reverse, rseq, into or reduce.
;;
;; Hint: (loop [remaining coll, acc '()] ...). `conj` onto a list adds to the
;;       front. The JVM has no TCO - `recur` is how you loop without growing
;;       the stack (and the compiler checks it's really in tail position).
(defn my-reverse [coll]
  (loop [remaining coll
         acc '()]
    (if (empty? remaining)
      acc
      (recur (rest remaining) (conj acc (first remaining))))))

;; -----------------------------------------------------------------------------
;; 09. fibs
;; Return an INFINITE lazy sequence of Fibonacci numbers: 0 1 1 2 3 5 8 ...
;;   (take 7 (fibs)) ;=> (0 1 1 2 3 5 8)
;; Hint: (iterate f [0 1]) gives an infinite seq of pairs; then (map first ...).
;;       Use +' instead of + so big numbers auto-promote to BigInt
;;       (the test checks (nth (fibs) 100)).
(defn fibs []
  (map first
       (iterate (fn [[a b]] [b (+' a b)])
                [0 1])))

(defn fibs->> []
  (->> [0 1]
       (iterate (fn [[a b]] [b (+' a b)]))
       (map first)))

;; =============================================================================
;; Part 3 - Immutable data
;; =============================================================================

;; -----------------------------------------------------------------------------
;; 10. restock
;; Add qty to an item's quantity and return the NEW inventory. If the item
;; doesn't exist yet, create it with that quantity.
;;   (restock {:warehouse "A" :items {:apple {:qty 3}}} :apple 2)
;;   ;=> {:warehouse "A" :items {:apple {:qty 5}}}
;; Hint: update-in with a path vector [:items item :qty]; fnil again.
(defn restock [inventory item qty]
  (update-in inventory [:items item :qty] (fnil #(+ qty %) 0)))

;; -----------------------------------------------------------------------------
;; 11. oldest-names
;; Return the names of the n oldest people, oldest first.
;;   (oldest-names 2 [{:name "Ann" :age 31} {:name "Bob" :age 45} {:name "Cid" :age 22}])
;;   ;=> ("Bob" "Ann")
;; Hint: (sort-by keyfn comparator coll) - try `>` as the comparator.
;;       Then take and map. A ->> pipeline reads nicely here.
(defn oldest-names [n people]
  (->> people
       (sort-by :age >)
       (map :name)
       (take n)))

;; =============================================================================
;; Part 4 - Functions as values
;; =============================================================================

;; -----------------------------------------------------------------------------
;; 12. my-comp
;; Compose any number of one-argument functions, right to left, like `comp`.
;;   ((my-comp inc #(* 2 %)) 5) ;=> 11    ; (inc (* 2 5))
;;   ((my-comp) 42)             ;=> 42    ; no fns => identity
;; Rule: don't use clojure.core/comp.
;; Hint: fs is a seq of functions (or nil). Return a (fn [x] ...).
;;       `reduce` over (reverse fs), threading x through each one.
(defn my-comp [& fs]
  (fn [x]
    (reduce (fn [acc f] (f acc))
            x
            (reverse fs))))

;; -----------------------------------------------------------------------------
;; 13. make-counter
;; Return a zero-arg function that returns 1, 2, 3, ... on successive calls.
;; Every counter is independent.
;; Hint: a closure over an (atom 0). `swap!` returns the new value.
(defn make-counter []
  (let [counter (atom 0)]
    (fn []
      (swap! counter inc))))

;; =============================================================================
;; Part 5 - Polymorphism
;; =============================================================================

;; -----------------------------------------------------------------------------
;; 14. area (multimethod)
;; Dispatch on the :shape key:
;;   (area {:shape :circle :r 1})           ;=> 3.141592653589793
;;   (area {:shape :rect :w 2 :h 3})        ;=> 6
;;   (area {:shape :triangle :base 3 :h 3}) ;=> 9/2   <- yes, a Ratio. Use /
;; Unknown shapes throw (ex-info "Unknown shape" {:shape <the :shape value>}).
;; Hint: the defmulti is done - write one defmethod per shape, and fix
;;       :default. Math/PI is Java interop for a static field.
;; REPL gotcha: re-evaluating a defmulti does NOT redefine it (defonce
;;       semantics). To change the dispatch fn, (ns-unmap *ns* 'area) first.
(defmulti area :shape)

(defmethod area :circle [{:keys [r]}]
  (* Math/PI (square r)))

(defmethod area :rect [{:keys [w h]}]
  (* w h))

(defmethod area :triangle [{:keys [base h]}]
  (/ (* base h) 2))

(defmethod area :default [{:keys [shape]}]
  (throw (ex-info "Unknown shape" {:shape shape})))

;; -----------------------------------------------------------------------------
;; 15. Speaker (protocols & records)
;; Implement `speak` for both records, and for plain Java Strings:
;;   (speak (->Dog "Rex"))                           ;=> "Rex says woof"
;;   (speak (->Cat "Kit" false))                     ;=> "Kit says meow"
;;   (speak (map->Cat {:name "Tom" :grumpy? true}))  ;=> "Tom ignores you"
;;   (speak "hi")                                    ;=> "hi says nothing"
;; Hint: inside a record's method body, fields are plain locals (name, grumpy?).
;;       extend-protocol lets you bolt a protocol onto a type you don't own.
;;       Records are also maps - try (:name (->Dog "Rex")) and (assoc ...) at
;;       the REPL.
(defprotocol Speaker
  (speak [this]))

(defrecord Dog [name]
  Speaker
  (speak [_]
    (str name " says woof")))

(defrecord Cat [name grumpy?]
  Speaker
  (speak [_]
    (str name (if grumpy? " ignores you" " says meow"))))

(extend-protocol Speaker
  String
  (speak [s]
    (str s " says nothing")))

;; =============================================================================
;; Part 6 - Interop, errors, state
;; =============================================================================

;; -----------------------------------------------------------------------------
;; 16. parse-long-safe
;; Parse a string into a long. Return nil if it can't be parsed (or is nil).
;;   (parse-long-safe "42")  ;=> 42
;;   (parse-long-safe "4x2") ;=> nil
;; Rule: use Java interop (Long/parseLong s) and try/catch. (clojure.core has
;;       parse-long since 1.11 - check it out afterwards.)
;; Hint: (try ... (catch NumberFormatException _ nil))
(defn parse-long-safe [s]
  (try (Long/parseLong s) (catch NumberFormatException _ nil)))

;; -----------------------------------------------------------------------------
;; 17. transfer
;; accounts is a map of id -> balance. Move amount from `from` to `to` and
;; return the new map. If funds are insufficient, throw:
;;   (ex-info "Insufficient funds" {:account from :balance <balance> :amount amount})
;;   (transfer {:a 100 :b 0} :a :b 30) ;=> {:a 70 :b 30}
;; Hint: `let` the balance, `when`/`if` + throw, then -> with two `update`s.
(defn transfer [accounts from to amount]
  (let [balance (get accounts from)]
    (when (< balance amount)
      (throw (ex-info "Insufficient funds"
                      {:account from :balance balance :amount amount})))
    (-> accounts
        (update from - amount)
        (update to + amount))))

;; -----------------------------------------------------------------------------
;; 18. transfer!
;; Same, but accounts-atom is an ATOM. Apply the transfer atomically and return
;; the new state. Reuse `transfer` - that's the point: pure functions do the
;; work, and state changes happen in one small place.
;; Hint: (swap! a f x y z) calls (f current-value x y z) and retries on
;;       conflict. The test fires 90 concurrent futures at it.
(defn transfer! [accounts-atom from to amount]
  (swap! accounts-atom transfer from to amount))
;; =============================================================================
;; Part 7 - Macros
;; =============================================================================

;; -----------------------------------------------------------------------------
;; 19. unless
;; Evaluate body (like `do`) only when test is falsey; otherwise return nil.
;;   (unless false :yes)                     ;=> :yes
;;   (unless true (println "never printed")) ;=> nil
;; The body must NOT be evaluated when test is truthy - which is exactly why
;; this has to be a macro and can't be a function.
;; Hint: syntax-quote `, unquote ~, unquote-splicing ~@. Check your output:
;;         (macroexpand-1 '(unless x (a) (b)))
;; (The stub expands to a (todo) call so the test file still compiles.)
(defmacro unless [test & body]
  `(todo))
