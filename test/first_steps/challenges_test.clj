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

(deftest c19-unless-test
  (is (= :yes (ch/unless false :yes)))
  (is (= :yes (ch/unless nil :ignored :yes)) "body behaves like `do`")
  (is (nil? (ch/unless true :nope)))
  (testing "body is not evaluated when test is truthy"
    (let [calls (atom 0)]
      (ch/unless true (swap! calls inc))
      (is (= 0 @calls)))))
