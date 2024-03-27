(ns rpg.magical-items
  (:require [rpg.common :refer [HasHealth max-health]]))

(defrecord ^:private Item [health max-health]
  HasHealth
  (alive? [self] (> (:health self) 0))
  (add-health [self delta]
    (let [hp (max 0 (min (+ (:health self) delta) (:max-health self)))]
    (assoc self :health hp))))

(defmethod max-health Item [self] (:max-health self))

(defn potion [hp]
  (->Item hp hp))

(defn weapon [hp]
  (->Item hp hp))