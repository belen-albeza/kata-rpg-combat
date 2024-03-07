(ns rpg.actions
  (:require [rpg.common :as c]))

(defprotocol Action
  (run [self]))

(defrecord AttackAction [source target base-damage]
  Action
  (run [self]
    (assert (and (satisfies? c/HasID source) (satisfies? c/HasLevel source) (satisfies? c/HasHealth source)) "invalid attacker")
    (assert (and (satisfies? c/HasID target) (satisfies? c/HasLevel target) (satisfies? c/HasHealth target)) "invalid target")
    (when (= (c/uid source) (c/uid target)) (throw (Exception. "characters cannot attack themselves")))
    (when-not (c/alive? source) (throw (Exception. "dead characters cannot attack")))
    (let [level-diff (- (c/level source) (c/level target))
          damage-modifier (if (>= level-diff 5) 1.5 (if (<= level-diff -5) 0.5 1.0))
          damage (int (* -1 (* base-damage damage-modifier)))
          updated-target (c/add-health target damage)]
      [source updated-target damage])))

(defrecord HealAction [source target hp]
  Action
  (run [self]
    (assert (satisfies? c/HasID source) "invalid healer")
    (assert (and (satisfies? c/HasID target) (satisfies? c/HasHealth target)) "invalid target")
    (when-not (c/alive? source) (throw (Exception. "dead characters cannot heal")))
    (let [updated-target (c/add-health target hp)]
      [source updated-target hp])))