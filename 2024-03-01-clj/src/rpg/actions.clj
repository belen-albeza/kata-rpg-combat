(ns rpg.actions
  (:require [rpg.common :as c]))

(defprotocol Action
  (run [self]))

(defrecord ^:private AttackAction [source target base-damage alliances]
  Action
  (run [self]
    (let [level-diff (- (c/level source) (c/level target))
          damage-modifier (if (>= level-diff 5) 1.5 (if (<= level-diff -5) 0.5 1.0))
          damage (int (* -1 (* base-damage damage-modifier)))
          updated-target (c/add-health target damage)]
      [source updated-target damage])))

(defrecord ^:private HealAction [source target hp alliances]
  Action
  (run [self]
    (let [updated-target (c/add-health target hp)]
      [source updated-target hp])))

(defn attack [source target damage & [alliances]]
  (assert (and (satisfies? c/HasID source) (satisfies? c/HasLevel source) (satisfies? c/HasHealth source)) "invalid attacker")
  (assert (and (satisfies? c/HasID target) (satisfies? c/HasLevel target) (satisfies? c/HasHealth target)) "invalid target")
  (assert (or (nil? alliances) (and (some? alliances) (satisfies? c/HasAlliances alliances))) "invalid alliances")

  (when (= (c/uid source) (c/uid target)) (throw (Exception. "attackers cannot attack themselves")))
  (when (and (some? alliances) (c/allies? alliances source target)) (throw (Exception. "attackers cannot target allies")))
  (when-not (c/alive? source) (throw (Exception. "dead attackers cannot attack")))

  (->AttackAction source target damage alliances))

(defn heal [source target hp & [alliances]]
  (assert (satisfies? c/HasID source) "invalid healer")
  (assert (and (satisfies? c/HasID target) (satisfies? c/HasHealth target)) "invalid target")
  (assert (or (nil? alliances) (and (some? alliances) (satisfies? c/HasAlliances alliances))) "invalid alliances")

  (when-not (c/alive? source) (throw (Exception. "dead healers cannot heal")))
  (when (and (some? alliances) (not (c/allies? alliances source target))) (throw (Exception. "healers cannot target non-allies")))
  (when (and (nil? alliances) (not= (c/uid source) (c/uid target))) (throw (Exception. "healers cannot target non-allies")))
  (when-not (c/alive? target) (throw (Exception. "healers cannot target dead allies")))

  (->HealAction source target hp alliances))
