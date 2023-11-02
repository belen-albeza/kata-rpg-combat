(ns rpg-combat.character)

(defn character [name & {:keys [health level] :or {health 1000 level 1}}]
  {:name name :health health :level level})

(defn alive? [chara]
  (> (:health chara) 0))