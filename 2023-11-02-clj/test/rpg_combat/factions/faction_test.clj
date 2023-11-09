(ns rpg-combat.factions.faction-test
  (:require [clojure.test :refer :all]
            [rpg-combat.factions.faction :refer :all]))

(defn- any-faction-with-members [list]
  (let [f (faction :foo)]
    (assoc f :members list)))

(deftest faction-creation
  (testing "factions are created with an id"
    (let [f (faction :horde)]
      (is (= (:id f) :horde)))))

(deftest faction-add
  (testing "adds a new member"
    (let [
      f (faction :horde)
      f (add-member f "foo")]

      (is (true? (has-member? f "foo")))))

  (testing "does not add the same member twice"
     (let [
      f (faction :horde)
      f (add-member f "foo")
      f (add-member f "foo")
      members (:members f)]

      (is (= (count members) 1))
      (is (true? (has-member? f "foo"))))))

(deftest faction-remove
  (testing "removes a member"
    (let [
      f (any-faction-with-members #{"foo" "bar"})
      f (remove-member f "foo")
      members (:members f)]

    (is (= (count members) 1))
    (is (false? (has-member? f "foo"))))))