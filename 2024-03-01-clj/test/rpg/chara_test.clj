(ns rpg.chara-test
  (:require [clojure.test :refer :all]
            [rpg.common :refer :all]
            [rpg.chara :refer :all]))

(deftest character-creation
  (testing "Character is created with the given ID"
    (let [c (character :foo)]
      (is (= (:id c) :foo))))

  (testing "Character is created with a default of 1000 health and level 1"
    (let [c (character :any)]
      (is (= (:health c) 1000))
      (is (= (:level c) 1))))

  (testing "Character is created with the given health value"
    (let [c (character :any {:health 123})]
      (is (= (:health c) 123))))

  (testing "Character is created with the given level")
    (let [c (character :any {:level 5})]
      (is (= (:level c) 5))))

(deftest character-health
  (testing "Character returns whether they are alive or not"
    (let [alive-chara (character :alive {:health 1})
          dead-chara (character :dead {:health 0})]
      (is (= true (alive? alive-chara)))
      (is (= false (alive? dead-chara)))))

  (testing "Maximum health is 1000 for level 5 and lower"
    (let [c (character :any {:level 5})]
      (is (= (max-health c) 1000))))

  (testing "Maximum health is 1500 for level 6 and higher")
    (let [c (character :any {:level 6})]
      (is (= (max-health c) 1500)))

  (testing "A character's health cannot be reduced below zero"
    (let [c (character :any {:health 20})
          c (add-health c -25)]
      (is (= (:health c) 0))))

  (testing "A character's health cannot go above their maximum health"
    (let [low-level-chara (character :any {:health 999 :level 1})
          high-level-chara (character :any {:health 1499 :level 6})
          low-level-chara (add-health low-level-chara 10)
          high-level-chara (add-health high-level-chara 10)]
      (is (= (:health low-level-chara) 1000))
      (is (= (:health high-level-chara) 1500)))))
