(ns rpg.factions-test
  (:require [clojure.test :refer :all]
            [rpg.chara :refer [HasID]]
            [rpg.factions :refer :all]))

(defrecord Member [id]
  HasID
  (uid [self] (:id self)))

(defn- any-member [] (->Member :any))

(deftest faction-manager-creation
  (testing "Manager is created with an empty list of factions as default"
    (let [fm (faction-manager)
          list (factions fm)]
      (is (= (count list) 0))))

  (testing "Manager is created with the given factions"
    (let [fm (faction-manager {:horde [:garrosh :thrall] :alliance [:anduin]})
          list (factions fm)]
      (is (= (count list) 2))
      (is (contains? list :alliance))
      (is (contains? list :horde)))))

(deftest faction-manager-factions
  (testing "Managers create a faction with the given id"
    (let [fm (faction-manager)
          fm (add fm :horde)]
      (is (= (count (factions fm)) 1))
      (is (contains? (factions fm) :horde))))

  (testing "An exception is raised when we try create an already-existing faction"
    (let [fm (faction-manager {:horde #{}})]
      (is (thrown-with-msg? Exception #"already exists" (add fm :horde))))))

(deftest faction-manager-belongs
  (testing "Returns whether an entity with an ID belongs to a faction or not"
    (let [fm (faction-manager {:horde #{}})]
      (is (= false (belongs? fm :horde (any-member)))))
    (let [fm (faction-manager {:horde #{:any}})]
      (is (= true (belongs? fm :horde (any-member))))))
  (testing "Raises an exception when trying to check a non-existing faction"
    (let [fm (faction-manager)]
      (is (thrown-with-msg? Exception #"does not exist" (belongs? fm :horde (any-member)))))))

(deftest faction-manager-join-faction
  (testing "An entity with ID can join an existing faction"
    (let [fm (faction-manager {:horde #{}})
          fm (join fm :horde (any-member))]
      (is (belongs? fm :horde (any-member)))))
  (testing "Raises an exception when trying to join a non-existing faction"
    (let [fm (faction-manager)]
      (is (thrown-with-msg? Exception #"does not exist" (join fm :horde (any-member)))))))

(deftest faction-manager-join-faction
  (testing "A member can leave a faction"
    (let [fm (faction-manager {:horde #{:garrosh}})
          fm (leave fm :horde (->Member :garrosh))]
      (is (not (belongs? fm :horde (->Member :garrosh))))))
  (testing "Raises an exception when trying to leave a non-existing faction"
    (let [fm (faction-manager)]
      (is (thrown-with-msg? Exception #"does not exist" (leave fm :horde (any-member)))))))

(deftest faction-manager-allies
  (testing "Two members that have a faction in common are allies"
    (let [fm (faction-manager {:horde #{:garrosh :thrall}})]
      (is (allies? fm (->Member :garrosh) (->Member :thrall)))))
  (testing "Two members with no faction in common are not allies"
    (let [fm (faction-manager {:horde #{:garrosh} :alliance #{:anduin}})]
      (is (not (allies? fm (->Member :garrosh) (->Member :anduin))))))
  (testing "Two members that don't belong to any faction are not allies"
    (let [fm (faction-manager)]
      (is (not (allies? fm (->Member :garrosh) (->Member :anduin)))))))