(ns rpg-combat.actions-test
  (:require [clojure.test :refer :all]
            [rpg-combat.actions :refer :all]))

(defn- character [& {:keys [health level] :or {health 1 level 1}}]
  {:health health :level level})

(deftest character-attack
  (testing "a character deals damage to another character"
    (let [
      src (character)
      target (character :health 1000)
      damage 50
      [src, target, outcome]  (attack src target damage)]

      (is (= (:health target) 950))
      (is (= (:damage outcome) 50))))

  (testing "health does not drop below zero when receiving damage"
      (let [
        src (character)
        target (character :health 1)
        damage 50
        [src, target]  (attack src target damage)]

        (is (= (:health target) 0))))

  (testing "a character cannot attack themselves"
    (let [
      c (character)
      damage 50]
      (is (thrown-with-msg? AssertionError #"valid-attack-target?" (attack c c damage)))))

  (testing "a dead character cannot attack"
    (let [
      src (character :health 0)
      target (character)
      damage 50]
      (is (thrown-with-msg? AssertionError #"alive?" (attack src target damage))))))

(deftest character-heal
  (testing "a character can heal themselves"
    (let [
      c (character :health 100)
      hp 50
      [_, c, outcome] (heal c c hp)]
      (is (= (:health c) 150))
      (is (= (:hp outcome) 50))))

  (testing "characters cannot be healed above their max health"
    (let [
      level-5-chara (character :health 999 :level 5)
      level-6-chara (character :health 1499 :level 6)
      hp 50
      [_, level-5-chara] (heal level-5-chara level-5-chara hp)
      [_, level-6-chara] (heal level-6-chara level-6-chara hp)]
      (is (= (:health level-5-chara) 1000))
      (is (= (:health level-6-chara) 1500))))

  (testing "a dead character cannot heal"
    (let [
      c (character :health 0)
      hp 50]
      (is (thrown-with-msg? AssertionError #"alive?" (heal c c hp)))))

  (testing "a character cannot heal other characters"
    (let [
      src (character)
      other (character :health 100)
      hp 50]
      (is (thrown-with-msg? AssertionError #"valid-heal-target?" (heal src other hp))))))