(ns rpg-combat.core-test
  (:require [clojure.test :refer :all]
            [rpg-combat.core :refer :all]))

(deftest character-creation
  (testing "character gets created with 1000 health"
    (let [c (character "Garrosh")]
      (is (= (:health c) 1000)))))
