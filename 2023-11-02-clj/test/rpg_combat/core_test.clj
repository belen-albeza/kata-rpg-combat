(ns rpg-combat.core-test
  (:require [clojure.test :refer :all]
    [rpg-combat.character :as chara]
    [rpg-combat.actions :as actions]
    [rpg-combat.factions.faction-manager :as factions]
    [rpg-combat.core :refer :all]))

(deftest core-attack-test
  (testing "A character can attack another"
    (let [
      orc (chara/character "Garrosh")
      elf (chara/character "Malfurion" :health 1000)
      [orc elf _] (actions/attack orc elf 50)]

    (is (= (:health elf) 950))))

  (testing "A character cannot attack allies"
    (let [
      orc (chara/character "Garrosh")
      orc2 (chara/character "Thrall")
      fm (factions/faction-manager)
      fm (factions/add-faction fm :horde)
      fm (factions/join fm :horde orc)
      fm (factions/join fm :horde orc2)
      allies? (partial factions/allies? fm)]

      (is (thrown? AssertionError (actions/attack orc orc2 50 :allies? allies?))))))

(deftest core-heal-test
  (testing "A character can heal themselves"
    (let [
      orc (chara/character "Garrosh" :health 900)
      [_ orc _] (actions/heal orc orc 50)]

      (is (= (:health orc) 950))))

  (testing "A character cannot heal non-allies"
    (let [
      orc (chara/character "Garrosh")
      elf (chara/character "Malfurion" :health 900)
      fm (factions/faction-manager)
      allies? (partial factions/allies? fm)]

      (is (thrown? AssertionError (actions/heal orc elf 50 :allies? allies?)))))

  (testing "A character can heal allies"
    (let [
      orc (chara/character "Garrosh")
      orc2 (chara/character "Thrall" :health 900)
      fm (factions/faction-manager)
      fm (factions/add-faction fm :horde)
      fm (factions/join fm :horde orc)
      fm (factions/join fm :horde orc2)
      allies? (partial factions/allies? fm)
      [_ orc2 _] (actions/heal orc orc2 50 :allies? allies?)]

      (is (= (:health orc2) 950)))))
