(ns rpg-combat.factions.faction-manager
  (:require [clojure.set] [rpg-combat.factions.faction :as faction]))

(defn faction-manager [] {})

(defn add-faction [fm id]
  (let [f (faction/faction id)]
    (assoc fm id f)))

(defn factions [fm] fm)

(defn belongs? [fm faction-id member]
  {:pre [
    (contains? fm faction-id)
    (not (nil? (:id member)))]}

  (let [f (fm faction-id)]
    (faction/has-member? f (:id member))))

(defn join [fm faction-id member]
  {:pre [
    (contains? fm faction-id)
    (not (nil? (:id member)))]}

  (let [
    f (fm faction-id)
    f (faction/add-member f (:id member))]

    (assoc fm faction-id f)))

(defn leave [fm faction-id member]
  {:pre [
    (contains? fm faction-id)
    (not (nil? (:id member)))]}

  (let [
    f (fm faction-id)
    f (faction/remove-member f (:id member))]

    (assoc fm faction-id f)))

(defn- factions-for [fm member]
  (let [factions (vals fm)]
    (set (filter (fn [x] (faction/has-member? x (:id member))) factions))))

(defn allies? [fm member other]
  (let [
    factions-member (factions-for fm member)
    factions-other (factions-for fm other)
    common (clojure.set/intersection factions-member factions-other)]

    (> (count common) 0)))
