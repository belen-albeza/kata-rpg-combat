(ns rpg-combat.items.potion)

(defn potion [& {:keys [health] :or {health 100}}]
  {:health health})

(defn potion? [p]
  (not (nil? (:health p))))

(defn destroyed? [p]
  (<= (:health p) 0))

(defn consume [p max-hp]
  {:pre [(potion? p)]}
  (let [
    actual-hp (min max-hp (:health p))
    health (- (:health p) actual-hp)
    p (assoc p :health health)]

    [p {:hp actual-hp}]))
