(ns rpg-combat.factions.faction-manager-test
  (:require [clojure.test :refer :all]
            [rpg-combat.factions.faction-manager :refer :all]))

(deftest faction-manager-creation
  (testing "Creates a manager with no factions"
    (let [
      fm (faction-manager)
      factions (factions fm)]

      (is (empty? factions)))))


(deftest faction-manager-join
  (testing "Adds a member to the given faction"
    (let [
      fm (faction-manager)
      fm (add-faction fm :horde)
      fm (join fm :horde {:id "Garrosh"})
    ]
    (is (true? (belongs? fm :horde {:id "Garrosh"})))))

  (testing "Cannot add non-characters to a faction"
    (let [
      fm (faction-manager)
      fm (add-faction fm :horde)
      non-chara {:foo "bar"}]

      (is (thrown-with-msg? AssertionError #"valid-member" (join fm :horde non-chara))))))

(deftest faction-manager-leave
  (testing "Removes a member from the given faction"
     (let [
      fm (faction-manager)
      fm (add-faction fm :horde)
      fm (join fm :horde {:id "Garrosh"})
      fm (leave fm :horde {:id "Garrosh"})
    ]
    (is (false? (belongs? fm :horde {:id "Garrosh"}))))))

(deftest faction-manager-allies
  (testing "Returns true when two members belong to the same faction"
    (let [
      fm (faction-manager)
      fm (add-faction fm :horde)
      fm (join fm :horde {:id "Garrosh"})
      fm (join fm :horde {:id "Thrall"})
    ]

    (is (true? (allies? fm {:id "Garrosh"} {:id "Thrall"})))))

  (testing "Returns false when two members don't have any faction in common"
    (let [
      fm (faction-manager)
      fm (add-faction fm :horde)
      fm (add-faction fm :alliance)
      fm (join fm :horde {:id "Garrosh"})
      fm (join fm :alliance {:id "Anduin"})]

    (is (false? (allies? fm {:id "Garrosh"} {:id "Anduin"})))))

  (testing "Returns false if a character doesn't belong to any faction"
    (let [
      fm (faction-manager)
      fm (add-faction fm :horde)
      fm (join fm :horde {:id "Garrosh"})]

      (is (false? (allies? fm {:id "Garrosh"} {:id "Anduin"}))))))