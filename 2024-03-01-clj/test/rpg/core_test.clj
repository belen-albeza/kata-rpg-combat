(ns rpg.core-test
  (:require [clojure.test :refer :all]
            [rpg.chara :as chara]
            [rpg.factions :as factions]
            [rpg.core :refer :all]))

(deftest factions-test
  (testing "A character can join a faction"
    (let [orc (chara/character :garrosh)
          fm (factions/faction-manager)
          fm (factions/add fm :horde)
          fm (factions/join fm :horde orc)]
      (is (factions/belongs? fm :horde orc)))))