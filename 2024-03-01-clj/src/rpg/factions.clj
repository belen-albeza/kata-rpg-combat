(ns rpg.factions
  (:require [rpg.common :refer [HasID uid]]
            [clojure.set :as set]))

(defprotocol FactionManager
  (factions [self])
  (add [self faction])
  (belongs? [self faction member])
  (join [self faction member])
  (leave [self faction member])
  (allies? [self member other]))

(defn- members-of [manager faction]
  (assert (keyword? faction) "faction ID needs to be a keyword")
  (let [all-factions (:factions manager)
        members (all-factions faction)]
        (when (nil? members) (throw (Exception. "faction does not exist")))
        members))

(defn- factions-for [manager member]
  (assert (satisfies? HasID member) "invalid member")
  (let [uid (uid member)
        all-factions (:factions manager)]
        (set (for [[f m] all-factions :when (contains? m uid)] f))))

(defrecord Manager [factions]
  FactionManager
  (factions [self]
    (set (keys (:factions self))))

  (add [self uid]
    (assert (keyword? uid) "faction ID needs to be a keyword")
    (let [list (:factions self)
          _ (when (list uid) (throw (Exception. "faction already exists")))
          list (assoc list uid #{})]
          (assoc self :factions list)))

  (belongs? [self faction member]
    (let [all-members (members-of self faction)]
      (contains? all-members (uid member))))

  (join [self faction member]
    (assert (satisfies? HasID member) "invalid member")
    (let [all-members (members-of self faction)
          updated-members (conj all-members (uid member))
          updated-factions (assoc (:factions self) faction updated-members)]
      (assoc self :factions updated-factions)))

  (leave [self faction member]
    (assert (satisfies? HasID member) "invalid member")
    (let [all-members (members-of self faction)
          updated-members (disj all-members (uid member))
          updated-factions (assoc (:factions self) faction updated-members)]
      (assoc self :factions updated-factions)))

  (allies? [self member other]
    (let [factions-member (factions-for self member)
          factions-other (factions-for self other)
          common (set/intersection factions-member factions-other)]
      (> (count common) 0))))

(defn faction-manager [& [factions]]
  (let [factions (or factions {})]
   (->Manager factions)))
