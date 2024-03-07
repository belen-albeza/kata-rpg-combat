(ns rpg.core
 (:require [clojure.test :refer :all]
            [rpg.chara :as c]
            [rpg.actions :as actions]))
(defn -main
  "RPG Combat kata"
  [& args]
  (let [
    orc (c/character :garrosh)
    elf (c/character :malfurion)
    action (actions/->AttackAction orc elf 10)]
    (actions/run action))
  (println "RPG Combat kata"))
