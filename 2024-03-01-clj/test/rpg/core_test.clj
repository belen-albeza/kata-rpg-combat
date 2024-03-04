(ns rpg.core-test
  (:require [clojure.test :refer :all]
            [rpg.core :refer :all]))

(deftest character-creation
  (testing "Character is created with the given ID")
  (testing "Character is created with a default of 1000 health"
    (let [c (character :any)]
      (is (= (:health c) 1000))))
  (testing "Character is created with the given health value"
    (let [c (character :any {:health 123})]
      (is (= (:health c) 123)))))

(deftest character-health
  (testing "Character returns whether they are alive or not"
    (let [alive-chara (character :alive {:health 1})
          dead-chara (character :dead {:health 0})]
      (is (= true (alive? alive-chara)))
      (is (= false (alive? dead-chara))))))

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
      (is (thrown-with-msg? Exception #"cannot attack" (attack c other 10))))))

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