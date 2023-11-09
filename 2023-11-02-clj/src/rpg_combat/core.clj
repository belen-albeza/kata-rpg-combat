(ns rpg-combat.core
  (:require
    [rpg-combat.character :as chara]
    [rpg-combat.actions.attack :as actions.attack]
    [rpg-combat.actions.heal :as actions.heal]
    [rpg-combat.actions.item :as actions.item]
    [rpg-combat.factions.faction-manager :as factions]
    [rpg-combat.items.potion :as potion]
    [rpg-combat.items.weapon :as weapon])
  (:gen-class))

(defn -main
  [& args]
  (let [
    orc (chara/character "Garrosh")
    elf (chara/character "Elf")
    fm (factions/faction-manager)
    fm (factions/add-faction fm :horde)
    fm (factions/join fm :horde orc)
    [orc elf outcome] (actions.attack/attack orc elf 50)
    _ (println (:id orc ) "attacks" (:id elf) "and deals" (:damage outcome) "damage!")
    _ (println "\t" (str orc))
    _ (println "\t" (str elf))
    p (potion/potion :health 10)
    _ (println (:id elf) "drinks healing potion" (:health p) "hp")
    [elf p] (actions.item/use-item p elf)
    _ (println "\tPotion:" (str p))
    _ (println "\t" (str elf))
    w (weapon/weapon :health 2 :damage 100)
    _ (println (:id orc) "uses weapon" (str w))
    [orc elf w] (actions.item/use-item w orc elf)
    _ (println "\tWeapon:" (str w))
    _ (println "\t" (str orc))
    _ (println "\t" (str elf))
    [_ elf outcome] (actions.heal/heal elf elf 30)
    _ (println (:id elf ) "heals for" (:hp outcome) "!")
    _ (println "\t" (str elf))
    ]))
