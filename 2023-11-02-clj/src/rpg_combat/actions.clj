(ns rpg-combat.actions
  (:require [rpg-combat.core :as core]))

(defn- add-health [chara delta]
  (assoc chara :health (min (max 0 (+ (:health chara) delta)) 1000)))

(defn- valid-attack-target? [source target]
  (not (identical? source target)))

(defn- valid-heal-target? [source target]
  (identical? source target))

(defn attack [source target damage]
  {:pre [
    (core/alive? source)
    (valid-attack-target? source target)]}
  (let [updated-target (add-health target (- damage))]
    [source updated-target]))

(defn heal [source target hp]
  {:pre [
    (core/alive? source)
    (valid-heal-target? source target)]}
  (let [updated-target (add-health target hp)]
    [source updated-target]))
