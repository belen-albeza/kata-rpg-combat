(ns rpg-combat.character-test
  (:require [clojure.test :refer :all]
            [rpg-combat.character :refer :all]))

(deftest character-creation
  (testing "character gets created with max health"
    (let [
      level-5 (character "Garrosh" :level 1)
      level-6 (character "Garrosh" :level 6)]
      (is (= (:health level-5) 1000))
      (is (= (:health level-6) 1500))))

  (testing "character gets created with level 1 as default"
    (let [c (character "Garrosh")]
      (is (= (:level c) 1))))

  (testing "character gets created with custom starting values"
    (let [c (character "Garrosh" :health 200 :level 5)]
      (is (= (:health c) 200))
      (is (= (:level c) 5))))

  (testing "character gets created with health capped to their level"
    (let [
      level-5 (character "Garrosh" :health 2000 :level 5)
      level-6 (character "Garrosh" :health 2000 :level 6)]
      (is (= (:health level-5) 1000))
      (is (= (:health level-6) 1500)))))

(deftest character-alive?
  (testing "returns true when health is > 0"
    (let [c (character "Garrosh")]
      (is (true? (alive? c)))))

  (testing "returns false when health is zero"
    (let [c (character "Garrosh" :health 0)]
      (is (false? (alive? c))))))
