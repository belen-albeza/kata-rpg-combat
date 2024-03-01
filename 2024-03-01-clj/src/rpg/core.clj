(ns rpg.core
  (:gen-class))

(defprotocol HasHealth
  (alive? [self]))

(defrecord Chara [health]
  HasHealth
  (alive? [self] (> (:health self) 0)))

(defn character [& {:keys [health] :or {health 1000}}]
  (->Chara health))

(defn -main
  "RPG Combat kata"
  [& args]
  (println "RPG Combat kata"))
