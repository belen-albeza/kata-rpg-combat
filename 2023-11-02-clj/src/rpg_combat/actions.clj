(ns rpg-combat.actions
  (:require [rpg-combat.character :as character]))

(defn- valid-attack-target? [source target]
  (not (identical? source target)))

(defn- damage-modifier [source target]
  (let [diff (- (:level source) (:level target))]
    (cond
      (>= diff 5) 1.5
      (<= diff -5) 0.5
      :else 1)))

(defn attack [source target damage]
  {:pre [
    (character/alive? source)
    (valid-attack-target? source target)]}
  (let [
    total-damage (int (* (damage-modifier source target) damage))
    outcome {:damage total-damage}
    updated-target (character/add-health target (- total-damage))]
    [source updated-target outcome]))

(defn- valid-heal-target? [source target]
  (identical? source target))

(defn heal [source target hp]
  {:pre [
    (character/alive? source)
    (valid-heal-target? source target)]}
  (let [
    outcome {:hp hp}
    updated-target (character/add-health target hp)]
    [source updated-target outcome]))
