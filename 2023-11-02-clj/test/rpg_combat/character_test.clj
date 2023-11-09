(ns rpg-combat.character-test
  (:require [clojure.test :refer :all]
            [rpg-combat.character :refer :all]))

(deftest character-creation
  (testing "character gets created with the given id"
    (let [c (character "Garrosh")]
      (is (= (:id c) "Garrosh"))))

  (testing "character gets created with max health"
    (let [
      level-5 (character "Garrosh" :level 1)
      level-6 (character "Garrosh" :level 6)]
      (is (= (:health level-5) 1000))
      (is (= (:health level-6) 1500))))

  (testing "character gets created with level 1 as default"
    (let [c (character "Garrosh")]
      (is (= (:level c) 1))))

  (testing "characters gets created with zero xp as default"
    (let [c (character "Garrosh")]
      (is (= (:xp c) 0))))

  (testing "character must be created with a level between 1 and 10"
    (let [
      c1 (character "Rat" :level 0)
      c2 (character "Rat" :level 11)]
      (is (= (:level c1) 1))
      (is (= (:level c2) 10))))

  (testing "character gets created with custom starting values"
    (let [c (character "Garrosh" :health 200 :level 5 :xp 100)]
      (is (= (:health c) 200))
      (is (= (:level c) 5))
      (is (= (:xp c) 100))))

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

(deftest character-xp
  (testing "a character can earn xp"
    (let [
      c (character "Garrosh" :xp 0 :level 1)
      c (add-xp c 100)]

      (is (= (:xp c) 100))))

  (testing "dead characters cannot earn xp"
    (let [
      c (character "Garrosh" :xp 0 :level 1 :health 0)]
      c (add-xp c 100)

      (is (= (:xp c) 0))))

  (testing "characters level up when they earn enough xp"
    (let [
      c (character "Garrosh" :xp 0 :level 1)
      c (add-xp c 1000)]

      (is (= (:xp c) 0))
      (is (= (:level c) 2))))

  (testing "the amount of xp needed to level up increases with each level"
    (let [
      c1 (character "Garrosh" :xp 0 :level 2)
      c2 (character "Thrall" :xp 0 :level 2)
      c1 (add-xp c1 1000)
      c2 (add-xp c2 2000)]

      (is (= (:level c1) 2))
      (is (= (:level c2) 3)))))