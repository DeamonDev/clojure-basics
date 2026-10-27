(ns first-steps.challenges-test
  (:require [clojure.test :refer [deftest is testing are]]
            [first-steps.challenges :as ch]))

(deftest c01-square-test
  (are [x expected] (= expected (ch/square x))
    0  0
    4  16
    -3 9))

(deftest c02-classify-test
  (are [n expected] (= expected (ch/classify n))
    -3  :negative
    0   :zero
    7   :positive
    0.5 :positive))

(deftest c03-full-name-test
  (is (= "Ada Lovelace"
         (ch/full-name {:first-name "Ada" :last-name "Lovelace"})))
  (is (= "John Ronald Tolkien"
         (ch/full-name {:first-name "John" :middle-name "Ronald" :last-name "Tolkien"}))))

(deftest c03-full-name->>-test
  (is (= "Ada Lovelace"
         (ch/full-name->> {:first-name "Ada" :last-name "Lovelace"})))
  (is (= "John Ronald Tolkien"
         (ch/full-name->> {:first-name "John" :middle-name "Ronald" :last-name "Tolkien"}))))

(deftest c04-sum-of-odd-squares-test
  (is (= 35 (ch/sum-of-odd-squares [1 2 3 4 5])))
  (is (= 0 (ch/sum-of-odd-squares [2 4 6])))
  (is (= 0 (ch/sum-of-odd-squares []))))


(deftest c04-sum-of-odd-squares->>-test
  (is (= 35 (ch/sum-of-odd-squares->> [1 2 3 4 5])))
  (is (= 0 (ch/sum-of-odd-squares->> [2 4 6])))
  (is (= 0 (ch/sum-of-odd-squares->> []))))

(deftest c05-pairwise-diffs-test
  (is (= [3 5 7] (ch/pairwise-diffs [1 4 9 16])))
  (is (= [-1 0 2] (ch/pairwise-diffs [5 4 4 6])))
  (is (= [] (ch/pairwise-diffs [42])))
  (is (= [] (ch/pairwise-diffs []))))

(deftest c06-word-frequencies-test
  (is (= {"the" 2 "cat" 1 "hat" 1}
         (ch/word-frequencies "The cat. The hat!")))
  (is (= {} (ch/word-frequencies ""))))

(deftest c07-index-by-test
  (let [a {:id 1 :name "a"}
        b {:id 2 :name "b"}]
    (is (= {1 a 2 b} (ch/index-by :id [a b])))
    (is (= {"a" a "b" b} (ch/index-by :name [a b])))
    (is (= {} (ch/index-by :id [])))))

(deftest c08-my-reverse-test
  (is (= [3 2 1] (ch/my-reverse [1 2 3])))
  (is (= [] (ch/my-reverse [])))
  (testing "big input (plain recursion would overflow the stack)"
    (is (= 0 (last (ch/my-reverse (range 100000)))))))

(deftest c09-fibs-test
  (is (= [0 1 1 2 3 5 8 13 21 34] (take 10 (ch/fibs))))
  (testing "lazy, infinite, no overflow"
    (is (= 354224848179261915075N (nth (ch/fibs) 100)))))

(deftest c09-fibs->>-test
  (is (= [0 1 1 2 3 5 8 13 21 34] (take 10 (ch/fibs->>))))
  (testing "lazy, infinite, no overflow"
    (is (= 354224848179261915075N (nth (ch/fibs->>) 100)))))

(deftest c10-restock-test
  (let [inv {:warehouse "A" :items {:apple {:qty 3}}}]
    (is (= {:warehouse "A" :items {:apple {:qty 5}}}
           (ch/restock inv :apple 2)))
    (is (= {:warehouse "A" :items {:apple {:qty 3} :pear {:qty 4}}}
           (ch/restock inv :pear 4)))
    (testing "the original is untouched (this one passes for free - that's the point)"
      (ch/restock inv :apple 100)
      (is (= 3 (get-in inv [:items :apple :qty]))))))

(deftest c11-oldest-names-test
  (let [people [{:name "Ann" :age 31} {:name "Bob" :age 45} {:name "Cid" :age 22}]]
    (is (= ["Bob" "Ann"] (ch/oldest-names 2 people)))
    (is (= ["Bob" "Ann" "Cid"] (ch/oldest-names 10 people)))
    (is (= [] (ch/oldest-names 0 people)))))

(deftest c12-my-comp-test
  (is (= 11 ((ch/my-comp inc #(* 2 %)) 5)))
  (is (= 12 ((ch/my-comp #(* 2 %) inc) 5)))
  (is (= "3" ((ch/my-comp str count) [1 2 3])))
  (is (= 42 ((ch/my-comp) 42))))

(deftest c13-make-counter-test
  (let [c1 (ch/make-counter)
        c2 (ch/make-counter)]
    (is (= [1 2 3] [(c1) (c1) (c1)]))
    (is (= 1 (c2)) "counters are independent")))

(deftest c14-area-test
  (is (< (abs (- Math/PI (ch/area {:shape :circle :r 1}))) 1e-9))
  (is (== 6 (ch/area {:shape :rect :w 2 :h 3})))
  (is (= 9/2 (ch/area {:shape :triangle :base 3 :h 3})))
  (testing "unknown shape"
    (let [e (try (ch/area {:shape :hexagon})
                 (catch clojure.lang.ExceptionInfo e e))]
      (is (= "Unknown shape" (ex-message e)))
      (is (= {:shape :hexagon} (ex-data e))))))

(deftest c15-speaker-test
  (is (= "Rex says woof" (ch/speak (ch/->Dog "Rex"))))
  (is (= "Kit says meow" (ch/speak (ch/->Cat "Kit" false))))
  (is (= "Tom ignores you" (ch/speak (ch/map->Cat {:name "Tom" :grumpy? true}))))
  (is (= "hi says nothing" (ch/speak "hi"))))

(deftest c16-parse-long-safe-test
  (are [s expected] (= expected (ch/parse-long-safe s))
    "42"  42
    "-7"  -7
    "4x2" nil
    ""    nil
    nil   nil))

(deftest c17-transfer-test
  (is (= {:a 70 :b 30} (ch/transfer {:a 100 :b 0} :a :b 30)))
  (is (= {:a 0 :b 100} (ch/transfer {:a 100 :b 0} :a :b 100)))
  (testing "insufficient funds"
    (let [e (try (ch/transfer {:a 10 :b 0} :a :b 30)
                 (catch clojure.lang.ExceptionInfo e e))]
      (is (= "Insufficient funds" (ex-message e)))
      (is (= {:account :a :balance 10 :amount 30} (ex-data e))))))

(deftest c18-transfer!-test
  (let [accounts (atom {:a 100 :b 0})]
    (is (= {:a 90 :b 10} (ch/transfer! accounts :a :b 10)))
    (is (= {:a 90 :b 10} @accounts))
    (testing "concurrent transfers don't lose updates"
      (run! deref (doall (repeatedly 90 #(future (ch/transfer! accounts :a :b 1)))))
      (is (= {:a 0 :b 100} @accounts)))))

;; --- helpers for the concurrency challenges ---------------------------------

(defn- make-accounts [balances]
  (update-vals balances ref))

(defn- balances [accounts]
  (update-vals accounts deref))

(defn- elapsed-ms
  "Calls f and returns [result elapsed-milliseconds]."
  [f]
  (let [start  (System/nanoTime)
        result (f)]
    [result (/ (- (System/nanoTime) start) 1e6)]))

(defn- sleepy
  "A zero-arg fn that sleeps ms, then returns v."
  [ms v]
  (fn [] (Thread/sleep (long ms)) v))

;; -----------------------------------------------------------------------------

(deftest c19-transfer-stm!-test
  (let [accts (make-accounts {:a 100 :b 0})]
    (ch/transfer-stm! accts :a :b 30)
    (is (= {:a 70 :b 30} (balances accts))))
  (testing "insufficient funds: throws, and neither account changes"
    (let [accts (make-accounts {:a 10 :b 0})
          e     (try (ch/transfer-stm! accts :a :b 30)
                     (catch clojure.lang.ExceptionInfo e e))]
      (is (= "Insufficient funds" (ex-message e)))
      (is (= {:account :a :balance 10 :amount 30} (ex-data e)))
      (is (= {:a 10 :b 0} (balances accts)))))
  (testing "200 concurrent transfers in both directions keep every cent"
    (let [accts (make-accounts {:a 100 :b 100})]
      (->> (range 200)
           (mapv (fn [i]
                   (future
                     (if (even? i)
                       (ch/transfer-stm! accts :a :b 1)
                       (ch/transfer-stm! accts :b :a 1)))))
           (run! deref))
      (is (= {:a 100 :b 100} (balances accts))))))

(deftest c20-total-balance-test
  (is (= 100 (ch/total-balance (make-accounts {:a 70 :b 30}))))
  (testing "every snapshot is consistent while money is moving"
    (let [accts  (make-accounts {:a 100 :b 100 :c 0})
          move!  (fn [from to]
                   (dosync
                     (alter (accts from) dec)
                     (alter (accts to) inc)))
          movers (mapv (fn [[from to]]
                         (future (dotimes [_ 20000] (move! from to))))
                       [[:a :b] [:b :c] [:c :a]])
          totals (vec (repeatedly 2000 #(ch/total-balance accts)))]
      (run! deref movers)
      (is (every? #{200} totals)))))

(deftest c21-record-hit!-test
  (let [stats (ref {})]
    (ch/record-hit! stats "/home")
    (ch/record-hit! stats "/home")
    (ch/record-hit! stats "/about")
    (is (= {"/home" 2 "/about" 1} @stats))
    (testing "concurrent hits are all counted"
      (->> (range 100)
           (mapv (fn [_] (future (dotimes [_ 10] (ch/record-hit! stats "/home")))))
           (run! deref))
      (is (= 1002 (get @stats "/home"))))))

(deftest c22-agent-log-test
  (let [log (agent [])]
    (is (identical? log (ch/log! log "first")) "log! returns the agent")
    (is (= ["first"] (ch/read-log log)))
    (testing "messages sent from many threads all arrive"
      (->> (range 100)
           (mapv (fn [i] (future (ch/log! log i))))
           (run! deref))
      (let [entries (ch/read-log log)]
        (is (= 101 (count entries)))
        (is (= (set (range 100)) (set (rest entries))))))))

(deftest c23-run-parallel-test
  (is (= [] (ch/run-parallel [])))
  ;; A list, not a vector: vectors are realized lazily in chunks of 32, which
  ;; would hide the "deref while mapping" bug.
  (let [fs          (list (sleepy 200 :a) (sleepy 200 :b) (sleepy 200 :c)
                          (sleepy 200 :d) (sleepy 200 :e))
        [result ms] (elapsed-ms #(ch/run-parallel fs))]
    (is (= [:a :b :c :d :e] result))
    (is (vector? result))
    (is (< ms 600) "runs in parallel (~200ms), not one after another (~1000ms)")))

(deftest c24-with-timeout-test
  (is (= 42 (ch/with-timeout 500 (constantly 42))))
  (let [[result ms] (elapsed-ms #(ch/with-timeout 100 (sleepy 5000 42)))]
    (is (= :timeout result))
    (is (< ms 1000) "gives up after ~100ms"))
  (testing "a timed-out function is really cancelled"
    (let [finished? (atom false)]
      (ch/with-timeout 50 (fn [] (Thread/sleep 300) (reset! finished? true)))
      (Thread/sleep 500)
      (is (false? @finished?)))))

(deftest c25-make-loader-test
  (let [calls   (atom 0)
        loader  (ch/make-loader (fn []
                                  (swap! calls inc)
                                  (Thread/sleep 50)
                                  {:port 8080}))
        results (->> (range 50)
                     (mapv (fn [_] (future (loader))))
                     (mapv deref))]
    (is (every? #{{:port 8080}} results))
    (is (= {:port 8080} (loader)))
    (is (= 1 @calls) "load-fn ran exactly once, despite 51 calls from 50 threads")))

(deftest c26-race-test
  (let [[result ms] (elapsed-ms #(ch/race [(sleepy 1000 :slow)
                                           (sleepy 50 :fast)
                                           (sleepy 1000 :slower)]))]
    (is (= :fast result))
    (is (< ms 500) "returns as soon as the fastest one is done")))

(deftest c27-square-all-async-test
  (is (= [1 4 9] (ch/square-all-async [1 2 3])))
  (is (= [] (ch/square-all-async [])))
  (is (= (mapv #(* % %) (range 1000)) (ch/square-all-async (range 1000)))))

(deftest c28-first-response-test
  (let [[result ms] (elapsed-ms #(ch/first-response 500 [(sleepy 300 :slow)
                                                         (sleepy 50 :fast)]))]
    (is (= :fast result))
    (is (< ms 250) "returns as soon as the fastest one answers"))
  (let [[result ms] (elapsed-ms #(ch/first-response 100 [(sleepy 1000 :slow)]))]
    (is (= :timeout result))
    (is (< ms 500) "gives up after ~100ms")))

(deftest c29-unless-test
  (is (= :yes (ch/unless false :yes)))
  (is (= :yes (ch/unless nil :ignored :yes)) "body behaves like `do`")
  (is (nil? (ch/unless true :nope)))
  (testing "body is not evaluated when test is truthy"
    (let [calls (atom 0)]
      (ch/unless true (swap! calls inc))
      (is (= 0 @calls)))))
