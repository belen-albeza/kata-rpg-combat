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


(deftest potion-drink
  (testing "A potion can be drunk for healing by consuming its hp"
    (let [
      p (potion :health 100)
      add-health (fn [hp] [{:health hp} {:hp hp}])
      [p metadata] (drink p add-health)]

      (is (= (:health p) 0))
      (is (= metadata {:health 100}))))

  (testing "A potion keeps any leftover hp after being drunk"
    (let [
      p (potion :health 100)
      add-health (fn [hp] [{:health 200} {:hp 25}])
      [p metadata] (drink p add-health)]

      (is (= (:health p) 75)))))
