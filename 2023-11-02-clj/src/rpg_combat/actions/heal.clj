(ns rpg-combat.actions.heal
  (:require [rpg-combat.character :as character]))

(defn- valid-heal-target? [source target allies?]
  (and
    (character/character? target)
    (or
      (identical? source target)
      (allies? source target))))

(defn heal [source target healing & {:keys [allies? ] :or { allies? (fn [_ _] false)}}]
  {:pre [
    (character/alive? source)
    (valid-heal-target? source target allies?)]}
  (let [
    updated-target (character/add-health target healing)
    outcome {:hp healing }]

    [source updated-target outcome]))
