(ns rpg-combat.actions.item-test
  (:require [clojure.test :refer :all]
            [rpg-combat.actions.item :refer :all]))

(defn- character [& {:keys [health level] :or {health 1 level 1}}]
  {:health health :level level})

(defn- potion [& {:keys [health] :or {health 1}}]
  {:health health})

(deftest character-use-item
  (testing "A character can use a potion to heal themselves"
    (let [
      chara (character :health 900)
      item (potion :health 20)
      [chara item outcome] (use-item chara item)]

      (is (= (:health item) 0))
      (is (= (:health chara) 920))
      (is (= outcome {:hp 20}))))

  (testing "A potion keeps any leftover hp after being used"
    (let [
      chara (character :health 999)
      item (potion :health 10)
      [chara item outcome] (use-item chara item)]

      (is (= (:health chara) 1000))
      (is (= (:health item) 9))))

  (testing "Depleted potions cannot be used"
    (let [
      chara (character :health 900)
      item (potion :health 0)]

      (is (thrown-with-msg? AssertionError #"valid-item?" (use-item chara item))))))
