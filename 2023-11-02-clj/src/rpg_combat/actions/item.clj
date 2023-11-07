(ns rpg-combat.actions.item
  (:require
    [rpg-combat.character :as character]
    [rpg-combat.items.potion :as potion]))

(defn- dispatch-item-kind [item & _]
  (cond
    (potion/potion? item) :potion))

(defmulti valid-item? dispatch-item-kind)

(defmethod valid-item? :potion [potion]
  (not (potion/destroyed? potion)))

(defmulti use-item dispatch-item-kind)

(defmethod use-item :potion [chara potion]
  {:pre [(valid-item? potion) (character/character? chara)]}
  (let [
    hp (- (character/max-health chara) (:health chara))
    [potion outcome] (potion/consume potion hp)
    actual-hp (:hp outcome)
    chara (character/add-health chara actual-hp)]

    [chara potion {:hp actual-hp}]))
