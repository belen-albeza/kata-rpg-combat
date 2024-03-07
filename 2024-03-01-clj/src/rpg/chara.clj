(ns rpg.chara
  (:require [rpg.common :refer [HasHealth, HasLevel, HasID]]))

(defrecord Chara [id health level])

(defmulti max-health class)
(defmethod max-health Chara [self] (if (>= (:level self) 6) 1500 1000))

(extend-type Chara
  HasID
  (uid [self] (:id self))
  HasLevel
  (level [self] (:level self))
  HasHealth
  (alive? [self] (> (:health self) 0))
  (add-health [self delta]
    (let [health (min (max 0 (+ (:health self) delta)) (max-health self))]
      (assoc self :health health))))

(defn character [id & {:keys [health level] :or {level 1}}]
  (let [c (->Chara id 1 level)
        health (if (some? health) (min (max-health c) health) (max-health c))]
    (assoc c :health health)))
