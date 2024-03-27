(ns rpg.magical-items
  (:require [rpg.common :refer [HasHealth max-health HasDamage]]))

(defrecord ^:private HealingItem [health max-health]
  HasHealth
  (alive? [self] (> (:health self) 0))
  (add-health [self delta]
    (let [hp (max 0 (min (+ (:health self) delta) (:max-health self)))]
    (assoc self :health hp))))

(defrecord ^:private DamagingItem [health max-health damage]
  HasHealth
  (alive? [self] (> (:health self) 0))
  (add-health [self delta]
    (let [hp (max 0 (min (+ (:health self) delta) (:max-health self)))]
    (assoc self :health hp)))
  HasDamage
  (damage [self] (:damage self)))

(defmethod max-health HealingItem [self] (:max-health self))
(defmethod max-health DamagingItem [self] (:max-health self))

(defn potion [hp]
  (->HealingItem hp hp))

(defn weapon [hp damage]
  (when (< damage 0) (throw (Exception. "weapons cannot deal negative damage")))
  (->DamagingItem hp hp damage))