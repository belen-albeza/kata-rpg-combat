(ns rpg-combat.factions.faction)

(defn faction [id]
  {:id id :members (set [])})

(defn add-member [f id]
  (let [all (:members f)]
    (assoc f :members (conj all id))))

(defn remove-member [f id]
  (let [all (:members f)]
    (assoc f :members (disj all id))))

(defn faction-manager [] {})

(defn add-faction [fm id]
  (let [f (faction id)]
    (assoc fm id f)))

(defn factions [fm] fm)