(ns rpg.actions-test
  (:require [clojure.test :refer :all]
            [shrubbery.core :refer :all]
            [rpg.actions :refer :all]
            [rpg.common :refer [HasHealth HasID HasLevel add-health]]))

(defn- any-attacker [& {:keys [level id health] :or {level 1 id :attacker health 1000}}]
  (stub HasLevel {:level level} HasID {:uid id} HasHealth {:alive? (> health 0)}))

(defn- any-target [& {:keys [health level id] :or {health 1000 level 1 id :target}}]
  (mock HasLevel {:level level} HasID {:uid id} HasHealth {:alive? (> health 0)}))

(defn- any-healer [& {:keys [id] :or {id :healer}}]
  (mock HasID {:uid id}))


(deftest actions-attack
  (testing "An attacker can deal damage to a target"
    (let [c (any-attacker)
          other (any-target)
          action (->AttackAction c other 10)
          [_ _ damage] (run action)]
      (is (= damage -10))
      (is (received? other add-health [-10]))))

  (testing "Attackers cannot attack themselves"
    (let [c (any-target)
      action (->AttackAction c c 10)]
      (is (thrown-with-msg? Exception #"cannot attack" (run action)))))

  (testing "A dead attacker cannot attack"
    (let [c (any-attacker {:health 0})
          other (any-target)
          action (->AttackAction c other 10)]
      (is (thrown-with-msg? Exception #"cannot attack" (run action)))))

  (testing "Attackers deal +50% damage when they attack a target that is 5 levels lower or more"
    (let [c (any-attacker {:level 6})
          other (any-target {:level 1})
          action (->AttackAction c other 100)
          [_ _ damage] (run action)]
          (is (= damage -150))))

  (testing "Attackers deal -50% damage when they attack a target that is 5 levels higher or more"
    (let [c (any-attacker {:level 1})
          other (any-target {:level 6})
          action (->AttackAction c other 100)
          [_ _ damage] (run action)]
          (is (= damage -50)))))

(deftest actions-heal
  (testing "A character can heal themselves"
    (let [c (any-target {:health 900})
          action (->HealAction c c 10)
          [_ _ hp] (run action)]
      (is (received? c add-health [10]))
      (is (= hp 10))))

  (testing "Dead characters cannot heal"
    (let [c (any-target {:health 0})
          action (->HealAction c c 10)]
      (is (thrown-with-msg? Exception #"cannot heal" (run action))))))