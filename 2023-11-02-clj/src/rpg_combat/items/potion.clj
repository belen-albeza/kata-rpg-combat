(ns rpg-combat.items.potion)

(defn potion [& {:keys [health] :or {health 100}}]
  {:health health})

(defn- potion? [p]
  (not (nil? (:health p))))

(defn destroyed? [p]
  (<= (:health p) 0))

(defn- consume-hp [p hp]
  (let [health (- (:health p) hp)]
    (assoc p :health health)))

(defn drink [p add-health]
  {:pre [(potion? p) (not (destroyed? p))]}

  (let [
    [chara outcome] (add-health (:health p))
    p (consume-hp p (:hp outcome))]

    [p chara]))