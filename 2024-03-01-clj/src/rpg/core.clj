(ns rpg.core
 (:require [clojure.test :refer :all]
            [rpg.chara :refer :all]))
(defn -main
  "RPG Combat kata"
  [& args]
  (let [
    orc (character :garrosh)
    elf (character :malfurion)]
    (attack orc elf 10))
  (println "RPG Combat kata"))
