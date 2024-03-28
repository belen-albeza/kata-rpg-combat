(ns rpg.magical-items-test
  (:require [clojure.test :refer :all]
            [shrubbery.core :refer :all]
            [rpg.common :refer :all]
            [rpg.magical-items :refer :all]))

(deftest magical-items-test
  (testing "Magical items have health"
    (let [p (potion 100)
          w (weapon 50 10)]
      (is (= (:health p) 100))
      (is (= (:health w) 50))))

  (testing "Magical items have their Max. health fixed at the time of creation"
    (let [p (potion 100)]
      (is (= (max-health p) 100))))

  (testing "Magical items are destroyed if health is zero"
    (let [p (potion 0)]
      (is (not (alive? p))))))

(deftest magical-weapons-test
  (testing "Magical weapons cannot have negative damage"
    (is (thrown-with-msg? Exception #"negative damage" (weapon 10 -50)))))
