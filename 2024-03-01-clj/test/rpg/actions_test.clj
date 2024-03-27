(ns rpg.actions-test
  (:require [clojure.test :refer :all]
            [shrubbery.core :refer :all]
            [rpg.actions :refer :all]
            [rpg.common :refer [HasHealth HasID HasLevel add-health HasAlliances]]))

(defn- any-attacker [& {:keys [level id health] :or {level 1 id :attacker health 1000}}]
  (stub HasLevel {:level level} HasID {:uid id} HasHealth {:alive? (> health 0)}))

(defn- any-target [& {:keys [health level id] :or {health 1000 level 1 id :target}}]
  (mock HasLevel {:level level} HasID {:uid id} HasHealth {:alive? (> health 0)}))

(defn- any-healer [& {:keys [id] :or {id :healer}}]
  (mock HasID {:uid id} HasHealth {:alive? true}))

(defn- any-potion [& {:keys [hp] :or {hp 100}}]
  (mock HasHealth))

(deftest actions-attack
  (testing "An attacker can deal damage to a target"
    (let [c (any-attacker)
          other (any-target)
          action (attack c other 10)
          [_ _ damage] (run action)]
      (is (= damage -10))
      (is (received? other add-health [-10]))))

  (testing "Attackers cannot attack themselves"
    (let [c (any-target)]
      (is (thrown-with-msg? Exception #"cannot attack" (attack c c 10)))))

  (testing "A dead attacker cannot attack"
    (let [c (any-attacker {:health 0})
          other (any-target)]
      (is (thrown-with-msg? Exception #"cannot attack" (attack c other 10)))))

  (testing "Attackers deal +50% damage when they attack a target that is 5 levels lower or more"
    (let [c (any-attacker {:level 6})
          other (any-target {:level 1})
          action (attack c other 100)
          [_ _ damage] (run action)]
          (is (= damage -150))))

  (testing "Attackers deal -50% damage when they attack a target that is 5 levels higher or more"
    (let [c (any-attacker {:level 1})
          other (any-target {:level 6})
          action (attack c other 100)
          [_ _ damage] (run action)]
          (is (= damage -50))))

  (testing "Attackers cannot target allies"
    (let [c (any-attacker)
          other (any-target)
          alliances (stub HasAlliances {:allies? true})]
          (is (thrown-with-msg? Exception #"cannot target allies" (attack c other 10 alliances)))))

  (testing "Attackers can target non-allies"
    (let [c (any-attacker)
          other (any-target)
          alliances (stub HasAlliances {:allies? false})
          action (attack c other 10 alliances)
          [_ _ damage] (run action)]
          (is (= damage) -10))))

(deftest actions-heal
  (testing "A healer can heal themselves"
    (let [c (any-target {:health 900})
          action (heal c c 10)
          [_ _ hp] (run action)]
      (is (received? c add-health [10]))
      (is (= hp 10))))

  (testing "Dead healers cannot heal"
    (let [c (any-target {:health 0})]
      (is (thrown-with-msg? Exception #"cannot heal" (heal c c 10)))))

  (testing "A healer can heal allies"
    (let [c (any-healer)
          other (any-target {:health 900})
          alliances (stub HasAlliances {:allies? true})
          action (heal c other 50 alliances)
          [_ _ hp] (run action)]
      (is (received? other add-health [50]))
      (is (= hp 50))))

  (testing "A healer cannot heal a dead ally"
    (let [c (any-healer)
          other (any-target {:health 0})
          alliances (stub HasAlliances {:allies? true})]

      (is (thrown-with-msg? Exception #"cannot target dead" (heal c other 50 alliances)))))

  (testing "A healer cannot heal non-allies"
    (let [c (any-healer)
          other (any-target)
          alliances (stub HasAlliances {:allies? false})]
      (is (thrown-with-msg? Exception #"cannot target non-allies" (heal c other 50 alliances))))))

(deftest actions-use-potion
  (testing "A healer can use a magical potion to heal")
    (let [c (any-healer)
          p ()]))