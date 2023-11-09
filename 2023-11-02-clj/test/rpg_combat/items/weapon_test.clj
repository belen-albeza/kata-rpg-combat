(ns rpg-combat.items.weapon-test
  (:require [clojure.test :refer :all]
            [rpg-combat.items.weapon :refer :all]))

(deftest weapon-creation
  (testing "A weapon is created with health and damage"
    (let [w (weapon :health 4 :damage 10)]
      (is (= (:health w) 4))
      (is (= (:damage w) 10)))))

(deftest weapon-destroyed
  (testing "A weapon is destroyed when it reaches zero health"
    (let [w (weapon :health 0)]
      (is (destroyed? w)))))

(deftest weapon-consume
  (testing "A weapon uses 1 health when it's consumed"
    (let [
      w (weapon :health 1)
      [w outcome] (consume w)]

      (is (= (:hp outcome) -1))
      (is (= (:health w) 0))))

  (testing "A weapon cannot be consumed if it's destroyed already"
    (let [w (weapon :health 0)]
      (is (thrown-with-msg? AssertionError #"destroyed" (consume w))))))
