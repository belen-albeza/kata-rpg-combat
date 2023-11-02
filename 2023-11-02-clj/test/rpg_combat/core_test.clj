(ns rpg-combat.core-test
  (:require [clojure.test :refer :all]
            [rpg-combat.core :refer :all]))

(deftest character-creation
  (testing "character gets created with 1000 health"
    (let [c (character "Garrosh")]
      (is (= (:health c) 1000)))))

(deftest character-is-alive
  (testing "returns true when health is > 0"
    (let [c (character "Garrosh")]
      (is (true? (is-alive c)))))
  (testing "returns false when health is zero"
    (let [c (character "Garrosh" :health 0)]
      (is (false? (is-alive c))))))

(deftest character-attack
  (testing "a character deals damage to another character"
    (let [
      src (character "Garrosh")
      target (character "Elf" :health 1000)
      damage 50
      [src, target]  (attack src target damage)]

      (is (= (:health target) 950)))))