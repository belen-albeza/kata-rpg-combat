(ns rpg.core-test
  (:require [clojure.test :refer :all]
            [rpg.core :refer :all]))

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
      (is (= (:health c) 1000))))

  (testing "Maximum health is 1500 for level 6 and higher")
    (let [c (character :any {:level 6})]
      (is (= (:health c) 1500))))

(deftest character-attack
  (testing "A character can deal damage to another one"
    (let [c (character :attacker)
          other (character :target {:health 100})
          [_ other _] (attack c other 10)]
      (is (= (:health other) 90))))

  (testing "A character's health cannot be reduced below zero after an attack"
    (let [c (character :attacker)
          other (character :target {:health 20})
          [_ other _]  (attack c other 25)]
      (is (= (:health other) 0))))

  (testing "A character cannot attack themselves"
    (let [c (character :attacker)]
      (is (thrown-with-msg? Exception #"cannot attack" (attack c c 10)))))

  (testing "A dead character cannot attack"
    (let [c (character :attacker {:health 0})
          other (character :target)]
      (is (thrown-with-msg? Exception #"cannot attack" (attack c other 10)))))

  (testing "Characters deal +50% damage when they attack a target that is 5 levels lower or more"
    (let [c (character :attacker {:level 6})
          other (character :target {:level 1 :health 1000})
          [_ other _] (attack c other 100)]
          (is (= (:health other) 850))))

  (testing "Characters deal -50% damage when they attack a target that is 5 levels higher or more"
    (let [c (character :attacker {:level 1})
          other (character :target {:level 6 :health 1000})
          [_ other _] (attack c other 100)]
          (is (= (:health other) 950)))))

(deftest character-healing
  (testing "A character can heal themselves"
    (let [c (character :healer, {:health 900})
          [c hp] (heal c 10)]
      (is (= (:health c) 910))
      (is (= hp 10))))

  (testing "A character cannot heal above 1000 hp"
    (let [c (character :healer {:health 999})
          [c _] (heal c 10)]
      (is (= (:health c) 1000))))

  (testing "Dead characters cannot heal"
    (let [c (character :healer {:health 0})]
      (is (thrown-with-msg? Exception #"cannot heal" (heal c 10))))))