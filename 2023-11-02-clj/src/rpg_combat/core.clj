(ns rpg-combat.core
  (:require
    [rpg-combat.character :as chara]
    [rpg-combat.actions.attack :as actions.attack]
    [rpg-combat.actions.heal :as actions.heal]
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
    [orc, elf, outcome] (actions.attack/attack orc elf 50)
    _ (println (:id orc ) "attacks" (:id elf) "and deals" (:damage outcome) "damage!")
    _ (println (str orc))
    _ (println (str elf))
    p (potion/potion :health 10)
    ;; [p elf] (potion/drink p (partial chara/add-health elf))
    ;; _ (println (:id elf) "drinks potion for" hp "hp!")
    ;; _ (println (str elf))
    ]))
