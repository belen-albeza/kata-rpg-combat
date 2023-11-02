(ns rpg-combat.character)

(defn character [name & {:keys [health] :or {health 1000}}]
  {:name name :health health})

(defn alive? [chara]
  (> (:health chara) 0))