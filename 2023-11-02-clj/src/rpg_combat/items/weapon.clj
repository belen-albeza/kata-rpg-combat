(ns rpg-combat.items.weapon)

(defn weapon [& {:keys [health damage] :or {health 10 damage 1}}]
  {:health health :damage damage})

(defn weapon? [w]
  (and
    (not (nil? (:health w)))
    (not (nil? (:damage w)))))

(defn destroyed? [w]
  (<= (:health w) 0))

(defn consume [w]
  {:pre [(weapon? w) (not (destroyed? w))]}
  (let [
    health (- (:health w) 1)
    w (assoc w :health health)]

    [w {:hp -1}]))
