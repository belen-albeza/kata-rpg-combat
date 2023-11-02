(ns rpg-combat.core
  (:gen-class))

(defn character [name]
  {:name name :health 1000})

(defn -main
  [& args]
  (let [orc (character "Garrosh")]
    (println (str orc))))
