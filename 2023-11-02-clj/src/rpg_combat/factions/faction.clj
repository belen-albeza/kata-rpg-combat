(ns rpg-combat.factions.faction)

(defn faction [id]
  {:id id :members (set [])})

(defn add-member [f id]
  (let [all (:members f)]
    (assoc f :members (conj all id))))

(defn remove-member [f id]
  (let [all (:members f)]
    (assoc f :members (disj all id))))

(defn has-member? [f id]
  (contains? (:members f) id))