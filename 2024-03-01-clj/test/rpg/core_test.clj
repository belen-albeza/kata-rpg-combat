(ns rpg.core-test
  (:require [clojure.test :refer :all]
            [rpg.chara :as chara]
            [rpg.factions :as factions]
            [rpg.actions :as actions]
            [rpg.magical-items :as items]
            [rpg.core :refer :all]))

(deftest actions-test
  (testing "A character deals damage to another one by attacking"
    (let [orc (chara/character :garrosh)
          elf (chara/character :malfurion {:health 1000})
          attack (actions/attack orc elf 10)
          [_ elf damage]  (actions/run attack)]
      (is (= (:health elf) 990))
      (is (= damage -10))))

  (testing "A character can heal themselves")
    (let [orc (chara/character :garrosh :health 950)
          healing (actions/heal orc orc 10)
          [_ orc hp] (actions/run healing)]
      (is (= (:health orc) 960))
      (is (= hp 10)))

  (testing "A character can attack another with a magical weapon"
    (let [orc (chara/character :garrosh)
          elf (chara/character :malfurion {:health 1000})
          axe (items/weapon 10 100)
          attack (actions/attack-with-weapon orc elf axe)
          [_ elf axe damage] (actions/run attack)]
      (is (= (:health elf) 900))
      (is (= damage -100))
      (is (= (:health axe) 9)))))

(deftest factions-test
  (testing "A character can join a faction"
    (let [orc (chara/character :garrosh)
          fm (factions/faction-manager)
          fm (factions/add fm :horde)
          fm (factions/join fm :horde orc)]
      (is (factions/belongs? fm :horde orc))))

  (testing "A character can attack a non-ally"
    (let [orc (chara/character :garrosh)
          elf (chara/character :malfurion {:health 1000})
          fm (factions/faction-manager)
          fm (factions/add fm :horde)
          fm (factions/add fm :alliance)
          fm (factions/join fm :horde orc)
          fm (factions/join fm :alliance elf)
          attack (actions/attack orc elf 10 {:alliances fm})
          [orc elf damage] (actions/run attack)]
      (is (= damage -10))
      (is (= (:health elf) 990))))

  (testing "A character can heal an ally"
    (let [orc (chara/character :garrosh)
          troll (chara/character :thrall {:health 900})
          fm (factions/faction-manager)
          fm (factions/add fm :horde)
          fm (factions/join fm :horde orc)
          fm (factions/join fm :horde troll)
          healing (actions/heal orc troll 50 {:alliances fm})
          [orc troll hp] (actions/run healing)]
      (is (= hp 50))
      (is (= (:health troll) 950)))))

(deftest magical-items-test
  (testing "Characters cannot heal magical items"
    (let [orc (chara/character :garrosh)
          potion (items/potion 100)]
      (is (thrown-with-msg? AssertionError #"invalid target" (actions/heal orc potion 50)))))

  (testing "Magical items cannot belong to factions"
    (let [fm (factions/faction-manager)
          fm (factions/add fm :horde)
          potion (items/potion 50)]
      (is (thrown-with-msg? AssertionError #"invalid member" (factions/join fm :horde potion))))))
