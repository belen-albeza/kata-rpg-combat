(ns rpg.core-test
  (:require [clojure.test :refer :all]
            [rpg.core :refer :all]))

(deftest character-creation
  (testing "Character is created with a default of 1000 health"
    (let [c (character)]
      (is (= (:health c) 1000))))
  (testing "Character is created with the given health value"
    (let [c (character {:health 123})]
      (is (= (:health c) 123)))))

(deftest character-health
  (testing "Character returns whether they are alive or not"
    (let [alive-chara (character {:health 1})
          dead-chara (character {:health 0})]
      (is (= true (alive? alive-chara)))
      (is (= false (alive? dead-chara))))))
