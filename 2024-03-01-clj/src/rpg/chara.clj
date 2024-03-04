(ns rpg.chara (:gen-class))

(defprotocol HasID
  (uid [self]))

(defprotocol HasHealth
  (alive? [self])
  (add-health [self delta]))

(defprotocol HasLevel
  (level [self]))

(defprotocol Attacker
  (attack [self target damage]))

(defprotocol Healer
  (heal [self hp]))

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
      (assoc self :health health)))
  Attacker
  (attack [self target base-damage]
    (assert (and (satisfies? HasID target) (satisfies? HasHealth target) "invalid target"))
    (when (= (uid self) (uid target)) (throw (Exception. "characters cannot attack themselves")))
    (when-not (alive? self) (throw (Exception. "dead characters cannot attack")))
    (let [
      level-diff (- (:level self) (level target))
      damage-modifier (if (>= level-diff 5) 1.5 (if (<= level-diff -5) 0.5 1.0))
      damage (int (* base-damage damage-modifier))
      updated-target (add-health target (- damage))]
      [self updated-target damage]))
  Healer
  (heal [self hp]
    (when-not (alive? self) (throw (Exception. "dead characters cannot heal")))
    (let [self (add-health self hp)]
      [self hp])))

(defn character [id & {:keys [health level] :or {level 1}}]
  (let [c (->Chara id 1 level)
        health (if (some? health) (min (max-health c) health) (max-health c))]
    (assoc c :health health)))
