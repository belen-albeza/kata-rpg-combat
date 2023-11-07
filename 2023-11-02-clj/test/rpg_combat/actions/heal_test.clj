(ns rpg-combat.actions.heal-test
  (:require [clojure.test :refer :all]
            [rpg-combat.actions.heal :refer :all]))

(defn- character [& {:keys [health level] :or {health 1 level 1}}]
  {:health health :level level})

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
      (is (thrown-with-msg? AssertionError #"valid-heal-target?" (heal src other hp)))))

  (testing "a character cannot non-characters"
    (let [
      src (character)
      other {:health 100}
      hp 50]
      (is (thrown-with-msg? AssertionError #"valid-heal-target?" (heal src other hp))))))
