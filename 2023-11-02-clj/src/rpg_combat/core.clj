(ns rpg-combat.core
  (:gen-class))

(defn character [name & {:keys [health] :or {health 1000}}]
  {:name name :health health})

(defn is-alive [chara]
  (> (:health chara) 0))

(defn- add-health [chara delta]
  (assoc chara :health (max 0 (+ (:health chara) delta))))


(defn attack [source target damage]
  (when (= source target) (throw (ex-info "invalid target" {:character source})))
  (let [updated-target (add-health target (- damage))]
    [source updated-target]))

(defn -main
  [& args]
  (let [orc (character "Garrosh")]
    (println (str orc))))
