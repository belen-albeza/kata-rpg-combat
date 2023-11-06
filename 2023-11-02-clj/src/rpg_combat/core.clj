(ns rpg-combat.core
  (:require
    [rpg-combat.character :as chara]
    [rpg-combat.actions :as actions])
  (:gen-class))

(defn -main
  [& args]
  (let [
    orc (chara/character "Garrosh")
    elf (chara/character "Elf")
    [orc, elf, outcome] (actions/attack orc elf 50)]
    (println (:id orc ) "attacks" (:id elf) "and deals" (:damage outcome) "damage!")
    (println (str orc))
    (println (str elf))))
