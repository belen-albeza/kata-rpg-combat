(ns rpg-combat.items.potion-test
  (:require [clojure.test :refer :all]
            [rpg-combat.items.potion :refer :all]))

(deftest potion-creation
  (testing "Creates a potion with the given health"
    (let [p (potion :health 50)]
      (is (= (:health p) 50)))))

(deftest potion-destroyed?
  (testing "A potion is destroyed when its health reaches zero hp"
    (let [
      full (potion :health 1)
      depleted (potion :health 0)]

      (is (false? (destroyed? full)))
      (is (true? (destroyed? depleted))))))

(deftest potion-consume
  (testing "A potion can be used by consuming its hp"
    (let [
      p (potion :health 100)
      [p outcome] (consume p 20)]

      (is (= (:health p) 80))
      (is (= (:hp outcome) 20)))))
