(ns rpg-combat.core
  (:require
    [rpg-combat.character :as chara]
    [rpg-combat.actions :as actions]
    [rpg-combat.factions.faction-manager :as factions]
    [rpg-combat.items.potion :as potion])
  (:gen-class))

(defn -main
  [& args]
  (let [
    orc (chara/character "Garrosh")
    elf (chara/character "Elf")
    fm (factions/faction-manager)
    fm (factions/add-faction fm :horde)
    fm (factions/join fm :horde orc)
    [orc, elf, outcome] (actions/attack orc elf 50)
    _ (println (:id orc ) "attacks" (:id elf) "and deals" (:damage outcome) "damage!")
    _ (println (str orc))
    _ (println (str elf))
    p (potion/potion :health 10)
    hp (:health p)
    [p elf] (potion/drink p (partial chara/add-health elf))
    _ (println (:id elf) "drinks potion for" hp "hp!")
    _ (println (str elf))]))
