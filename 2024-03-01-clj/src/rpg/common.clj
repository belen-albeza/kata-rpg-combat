(ns rpg.common)

(defprotocol HasID
  (uid [self]))

(defprotocol HasHealth
  (alive? [self])
  (add-health [self delta]))

(defmulti max-health class)

(defprotocol HasLevel
  (level [self]))

(defprotocol HasAlliances
  (allies? [self entity other]))