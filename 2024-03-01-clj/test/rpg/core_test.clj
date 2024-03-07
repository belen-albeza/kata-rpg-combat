(ns rpg.core-test
  (:require [clojure.test :refer :all]
            [rpg.chara :as chara]
            [rpg.factions :as factions]
            [rpg.actions :as actions]
            [rpg.core :refer :all]))

(deftest actions-test
  (testing "A character deals damage to another one by attacking"
    (let [orc (chara/character :garrosh)
          elf (chara/character :malfurion {:health 1000})
          attack (actions/->AttackAction orc elf 10)
          [_ elf damage]  (actions/run attack)]
      (is (= (:health elf) 990))
      (is (= damage -10))))

  (testing "A character can heal themselves")
    (let [orc (chara/character :garrosh :health 950)
          healing (actions/->HealAction orc orc 10)
          [_ orc hp] (actions/run healing)]
      (is (= (:health orc) 960))
      (is (= hp 10))))

(deftest factions-test
  (testing "A character can join a faction"
    (let [orc (chara/character :garrosh)
          fm (factions/faction-manager)
          fm (factions/add fm :horde)
          fm (factions/join fm :horde orc)]
      (is (factions/belongs? fm :horde orc)))))