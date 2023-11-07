(ns rpg-combat.actions.attack-test
  (:require [clojure.test :refer :all]
            [rpg-combat.actions.attack :refer :all]))

(defn- character [& {:keys [health level] :or {health 1 level 1}}]
  {:health health :level level})

(deftest character-attack
  (testing "a character deals damage to another character"
    (let [
      src (character)
      target (character :health 1000)
      damage 50
      [src, target, outcome]  (attack src target damage)]

      (is (= (:health target) 950))
      (is (= (:damage outcome) 50))))

  (testing "health does not drop below zero when receiving damage"
      (let [
        src (character)
        target (character :health 1)
        damage 50
        [src, target]  (attack src target damage)]

        (is (= (:health target) 0))))

  (testing "a character cannot attack themselves"
    (let [
      c (character)
      damage 50]
      (is (thrown-with-msg? AssertionError #"valid-attack-target?" (attack c c damage)))))

  (testing "a dead character cannot attack"
    (let [
      src (character :health 0)
      target (character)
      damage 50]
      (is (thrown-with-msg? AssertionError #"alive?" (attack src target damage)))))

  (testing "a character cannot attack allies"
    (let [
      src (character)
      target (character)
      fn-allies? (fn [_ _] true)
      damage 50]

      (is (thrown-with-msg? AssertionError #"valid-attack-target?" (attack src target damage :allies? fn-allies?)))))

  (testing "attack deals 50% more damage when character attacks a target with 5+ less level"
    (let [
      src (character :level 6)
      target (character :health 1000 :level 1)
      damage 100
      [_, target] (attack src target damage)]

      (is (= (:health target) 850))))

   (testing "attack deals 50% less damage when character attacks a target with 5+ more level"
    (let [
      src (character :level 1)
      target (character :health 1000 :level 6)
      damage 100
      [_, target] (attack src target damage)]

      (is (= (:health target) 950)))))

