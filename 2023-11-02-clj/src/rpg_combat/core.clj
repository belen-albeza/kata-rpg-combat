(ns rpg-combat.core
  (:gen-class))

(defn character [name & {:keys [health] :or {health 1000}}]
  {:name name :health health})

(defn alive? [chara]
  (> (:health chara) 0))

(defn- add-health [chara delta]
  (assoc chara :health (min (max 0 (+ (:health chara) delta)) 1000)))

(defn- valid-attack-target? [source target]
  (not (= source target)))

(defn attack [source target damage]
  {:pre [
    (alive? source)
    (valid-attack-target? source target)]}
  (let [updated-target (add-health target (- damage))]
    [source updated-target]))

(defn heal [source target hp]
  {:pre [(alive? source)]}
  (let [updated-target (add-health target hp)]
    [source updated-target]))

(defn -main
  [& args]
  (let [orc (character "Garrosh")]
    (println (str orc))))
