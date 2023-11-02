(ns rpg-combat.actions
  (:require [rpg-combat.character :as character]))

(defn- valid-attack-target? [source target]
  (not (identical? source target)))

(defn- valid-heal-target? [source target]
  (identical? source target))

(defn attack [source target damage]
  {:pre [
    (character/alive? source)
    (valid-attack-target? source target)]}
  (let [
    outcome {:damage damage}
    updated-target (character/add-health target (- damage))]
    [source updated-target outcome]))

(defn heal [source target hp]
  {:pre [
    (character/alive? source)
    (valid-heal-target? source target)]}
  (let [
    outcome {:hp hp}
    updated-target (character/add-health target hp)]
    [source updated-target outcome]))
