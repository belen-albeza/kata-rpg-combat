(ns rpg-combat.core-test
  (:require [clojure.test :refer :all]
    [rpg-combat.character :as chara]
    [rpg-combat.actions.attack :as actions.attack]
    [rpg-combat.actions.heal :as actions.heal]
    [rpg-combat.actions.item :as actions.item]
    [rpg-combat.factions.faction-manager :as factions]
    [rpg-combat.items.potion :as potion]
    [rpg-combat.core :refer :all]))

(deftest core-attack-test
  (testing "A character can attack another"
    (let [
      orc (chara/character "Garrosh")
      elf (chara/character "Malfurion" :health 1000)
      [orc elf _] (actions.attack/attack orc elf 50)]

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

      (is (thrown? AssertionError (actions.attack/attack orc orc2 50 :allies? allies?))))))

(deftest core-heal-test
  (testing "A character can heal themselves"
    (let [
      orc (chara/character "Garrosh" :health 900)
      [_ orc _] (actions.heal/heal orc orc 50)]

      (is (= (:health orc) 950))))

  (testing "A character cannot heal non-allies"
    (let [
      orc (chara/character "Garrosh")
      elf (chara/character "Malfurion" :health 900)
      fm (factions/faction-manager)
      allies? (partial factions/allies? fm)]

      (is (thrown? AssertionError (actions.heal/heal orc elf 50 :allies? allies?)))))

  (testing "A character can heal allies"
    (let [
      orc (chara/character "Garrosh")
      orc2 (chara/character "Thrall" :health 900)
      fm (factions/faction-manager)
      fm (factions/add-faction fm :horde)
      fm (factions/join fm :horde orc)
      fm (factions/join fm :horde orc2)
      allies? (partial factions/allies? fm)
      [_ orc2 _] (actions.heal/heal orc orc2 50 :allies? allies?)]

      (is (= (:health orc2) 950))))

   (testing "A character cannot heal items"
    (let [
      orc (chara/character "Garrosh")
      potion (potion/potion :health 20)
      always-allies (fn [_ _] true)
      ]

      (is (thrown? AssertionError (actions.heal/heal orc potion 30 :allies? always-allies))))))

(deftest core-potions-test
  (testing "A character can heal by using a potion"
    (let [
      orc (chara/character "Garrosh" :health 900)
      potion (potion/potion :health 50)
      [orc potion _] (actions.item/use-item orc potion)]

      (is (potion/destroyed? potion))
      (is (= (:health orc) 950))))

  (testing "A potion keeps any leftover hp"
    (let [
      orc (chara/character "Garrosh" :health 999)
      potion (potion/potion :health 50)
      [orc potion _] (actions.item/use-item orc potion)]

      (is (= (:health potion) 49))
      (is (= (:health orc) 1000))))

  (testing "A potion cannot join a faction"
    (let [
      potion (potion/potion :health 50)
      fm (factions/faction-manager)
      fm (factions/add-faction fm :horde)]

    (is (thrown? AssertionError (factions/join fm :horde potion))))))

