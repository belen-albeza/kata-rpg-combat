(ns rpg-combat.core
  (:require [rpg-combat.character :as chara])
  (:gen-class))

(defn -main
  [& args]
  (let [orc (chara/character "Garrosh")]
    (println (str orc))))
