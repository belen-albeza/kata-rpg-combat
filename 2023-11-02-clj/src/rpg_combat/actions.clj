(ns rpg-combat.actions
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
    outcome {:damage total-damage}
    updated-target (character/add-health target (- total-damage))]
    [source updated-target outcome]))

(defn- valid-heal-target? [source target allies?]
  (or
    (identical? source target)
    (allies? source target)))

(defn heal [source target hp & {:keys [allies? ] :or { allies? (fn [_ _] false)}}]
  {:pre [
    (character/alive? source)
    (valid-heal-target? source target allies?)]}
  (let [
    outcome {:hp hp}
    updated-target (character/add-health target hp)]
    [source updated-target outcome]))
