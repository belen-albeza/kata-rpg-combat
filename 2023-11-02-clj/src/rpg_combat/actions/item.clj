(ns rpg-combat.actions.item
  (:require
    [rpg-combat.character :as character]
    [rpg-combat.actions.attack :as actions.attack]
    [rpg-combat.actions.heal :as actions.heal]
    [rpg-combat.items.potion :as potion]
    [rpg-combat.items.weapon :as weapon]))

(defn- dispatch-item-kind [item & _]
  (cond
    (weapon/weapon? item) :weapon
    (potion/potion? item) :potion))

(defmulti valid-item? dispatch-item-kind)

(defmethod valid-item? :potion [potion]
  (not (potion/destroyed? potion)))

(defmethod valid-item? :weapon [weapon]
  (not (weapon/destroyed? weapon)))

(defmulti use-item dispatch-item-kind)

(defmethod use-item :potion [potion chara]
  {:pre [(valid-item? potion) (character/character? chara)]}
  (let [
    hp (:health potion)
    [_ chara outcome] (actions.heal/heal chara chara hp)
    actual-hp (:hp outcome)
    [potion _] (potion/consume potion actual-hp)]

    [chara potion {:hp actual-hp}]))

(defmethod use-item :weapon [weapon source target]
  {:pre [(valid-item? weapon)]}
  (let [
    dmg (:damage weapon)
    [_ updated-target outcome] (actions.attack/attack source target dmg)
    [weapon _] (weapon/consume weapon)]
    [source updated-target weapon outcome]))

