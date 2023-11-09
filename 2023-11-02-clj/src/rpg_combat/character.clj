(ns rpg-combat.character)

(defn character? [chara]
  (and
    (number? (:health chara))
    (number? (:level chara))))

(defn max-health [chara]
  (if (<= (:level chara) 5) 1000 1500))

(defn add-health [chara delta]
  (let [
    max-hp (max-health chara)
    health (min (max 0 (+ (:health chara) delta)) max-hp)]

    (assoc chara :health health)))

(def ^:private MAX-LEVEL 10)

(defn- level-up [chara]
  (let [level (min (+ (:level chara) 1) MAX-LEVEL)]
    (assoc chara :level level)))

(defn- xp-for-level-up [chara]
  (let [
    levels (range (+ (:level chara) 1))]
  (* (reduce + levels) 1000)))

(defn add-xp [chara delta]
  (let [
    xp (+ (:xp chara) delta)
    chara (assoc chara :xp xp) ]
    (if (>= xp (xp-for-level-up chara)) (level-up chara) chara)))

(defn character [id & {:keys [health level xp] :or { health ##Inf level 1 xp 0}}]
  (let [
    level (min (max 1 level) MAX-LEVEL)
    max-hp (max-health {:level level})]

    {:id id :health (max (min health max-hp) 0) :level level :xp xp}))

(defn alive? [chara]
  (> (:health chara) 0))
