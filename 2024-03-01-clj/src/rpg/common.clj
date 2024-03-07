(ns rpg.common)

(defprotocol HasID
  (uid [self]))

(defprotocol HasHealth
  (alive? [self])
  (add-health [self delta]))

(defprotocol HasLevel
  (level [self]))
