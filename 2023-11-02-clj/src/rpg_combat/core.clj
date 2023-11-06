(ns rpg-combat.core
  (:require
    [rpg-combat.character :as chara]
    [rpg-combat.actions :as actions]
    [rpg-combat.factions.faction-manager :as factions])
  (:gen-class))

(defn -main
  [& args]
  (let [
    orc (chara/character "Garrosh")
    elf (chara/character "Elf")
    fm (factions/faction-manager)
    fm (factions/add-faction :horde)
    fm (factions/join fm :horde orc)
    [orc, elf, outcome] (actions/attack orc elf 50)]
    (println (:id orc ) "attacks" (:id elf) "and deals" (:damage outcome) "damage!")
    (println (str orc))
    (println (str elf))))
