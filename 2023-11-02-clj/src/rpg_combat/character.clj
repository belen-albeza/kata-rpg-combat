(ns rpg-combat.character)

(defn character? [chara]
  (and
    (not (nil? (:health chara)))
    (not (nil? (:level chara)))))

(defn max-health [chara]
  (if (<= (:level chara) 5) 1000 1500))

(defn add-health [chara delta]
  (let [
    max-hp (max-health chara)
    health (min (max 0 (+ (:health chara) delta)) max-hp)]

    (assoc chara :health health)))

(defn character [id & {:keys [health level] :or { health ##Inf level 1}}]
  (let [max-hp (max-health {:level level})]
    {:id id :health (max (min health max-hp) 0) :level level}))

(defn alive? [chara]
  (> (:health chara) 0))
