(ns rpg-combat.actions.attack
  (:require [rpg-combat.character :as character]))

(defn- valid-attack-target? [source target allies?]
  (and
    (not (identical? source target))
    (not (allies? source target))))

(defn- damage-modifier [source target]
  (let [diff (- (:level source) (:level target))]
    (cond
      (>= diff 5) 1.5
      (<= diff -5) 0.5
      :else 1)))

(defn attack [source target damage & {:keys [allies? ] :or { allies? (fn [_ _] false)}}]
  {:pre [
    (character/alive? source)
    (valid-attack-target? source target allies?)]}
  (let [
    total-damage (int (* (damage-modifier source target) damage))
    updated-target (character/add-health target (- total-damage))
    outcome {:damage total-damage }]

    [source updated-target outcome]))