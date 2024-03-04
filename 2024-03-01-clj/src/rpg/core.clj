(ns rpg.core
  (:gen-class))

(defprotocol HasID
  (uid [self]))

(defprotocol HasHealth
  (alive? [self])
  (add-health [self delta]))

(defprotocol Attacker
  (attack [self target damage]))

(defprotocol Healer
  (heal [self hp]))

(defrecord Chara [id health]
  HasID
  (uid [self] (:id self))
  HasHealth
  (alive? [self] (> (:health self) 0))
  (add-health [self delta] (assoc self :health (max 0 (+ (:health self) delta))))
  Attacker
  (attack [self target damage]
    (assert (and (satisfies? HasID target) (satisfies? HasHealth target) "invalid target"))
    (when (= (uid self) (uid target)) (throw (Exception. "characters cannot attack themselves")))
    (when (not (alive? self)) (throw (Exception. "dead characters cannot attack")))
    (let [updated-target (add-health target (- damage))]
      [self updated-target damage]))
  Healer
  (heal [self hp]
    (let [self (add-health self hp)]
      [self hp])))

(defn character [id & {:keys [health] :or {health 1000}}]
  (->Chara id health))

(defn -main
  "RPG Combat kata"
  [& args]
  (let [
    orc (character :garrosh)
    elf (character :malfurion)]
    (attack orc elf 10))
  (println "RPG Combat kata"))
