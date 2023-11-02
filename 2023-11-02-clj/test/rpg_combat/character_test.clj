(ns rpg-combat.character-test
  (:require [clojure.test :refer :all]
            [rpg-combat.character :refer :all]))

(deftest character-creation
  (testing "character gets created with 1000 health"
    (let [c (character "Garrosh")]
      (is (= (:health c) 1000)))))

(deftest character-alive?
  (testing "returns true when health is > 0"
    (let [c (character "Garrosh")]
      (is (true? (alive? c)))))
  (testing "returns false when health is zero"
    (let [c (character "Garrosh" :health 0)]
      (is (false? (alive? c))))))
