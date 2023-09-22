import { describe, it, expect } from "bun:test";

import { FactionManager } from ".";

const anyManagerWithFactions = (factions: string[]) => {
  const fm = new FactionManager();
  factions.forEach((x) => fm.add(x));
  return fm;
};

describe("FactionManager", () => {
  describe("Adding factions", () => {
    it("can add a new faction", () => {
      const fm = new FactionManager();

      fm.add("Horde");

      expect(fm.factions).toEqual(["Horde"]);
    });

    it("cannot add the same faction twice", () => {
      const fm = anyManagerWithFactions(["Horde"]);

      fm.add("Horde");

      expect(fm.factions).toEqual(["Horde"]);
    });
  });

  describe("Joining", () => {
    it("makes a member join a faction", () => {
      const fm = anyManagerWithFactions(["Horde"]);

      fm.join("Horde", "Garrosh");

      expect(fm.hasMember("Horde", "Garrosh")).toBeTrue();
    });

    it("makes a member join multiple factions", () => {
      const fm = anyManagerWithFactions(["Horde", "Alliance"]);

      fm.join("Alliance", "Sylvanas");
      fm.join("Horde", "Sylvanas");

      expect(fm.hasMember("Horde", "Sylvanas"));
      expect(fm.hasMember("Alliance", "Sylvanas"));
    });

    it("throws an error if faction does not exist", () => {
      const fm = anyManagerWithFactions([]);

      expect(() => fm.join("Horde", "Garrosh")).toThrow(/does not exist/);
    });
  });

  describe("Leaving", () => {
    it("makes a member leave a faction", () => {
      const fm = anyManagerWithFactions(["Horde"]);
      fm.join("Horde", "Garrosh");

      fm.leave("Horde", "Garrosh");

      expect(fm.hasMember("Horde", "Garrosh")).toBeFalse();
    });

    it("makes a member leave a faction even if they joined multiple times", () => {
      const fm = anyManagerWithFactions(["Horde"]);
      fm.join("Horde", "Garrosh");
      fm.join("Horde", "Garrosh");

      fm.leave("Horde", "Garrosh");

      expect(fm.hasMember("Horde", "Garrosh")).toBeFalse();
    });

    it("throws an error if faction does not exist", () => {
      const fm = anyManagerWithFactions([]);

      expect(() => fm.leave("Horde", "Garrosh")).toThrow(/does not exist/);
    });

    it("throws an error if member does not belong to faction", () => {
      const fm = anyManagerWithFactions(["Horde"]);
      expect(() => fm.leave("Horde", "Garrosh")).toThrow(/does not belong/);
    });
  });

  describe("Members", () => {
    it("returns whether someone belongs to a faction", () => {
      const fm = anyManagerWithFactions(["Horde"]);
      fm.join("Horde", "Garrosh");

      expect(fm.hasMember("Horde", "Garrosh")).toBeTrue();
      expect(fm.hasMember("Horde", "Anduin")).toBeFalse();
    });

    it("throws an error when checking for membership for a non-existing faction", () => {
      const fm = anyManagerWithFactions([]);
      expect(() => fm.hasMember("Horde", "Garrosh")).toThrow(/does not exist/);
    });
  });

  describe("Alliances", () => {
    it("marks two people as allies if they belong to the same faction", () => {
      const fm = anyManagerWithFactions(["Horde"]);

      fm.join("Horde", "Garrosh");
      fm.join("Horde", "Thrall");

      expect(fm.areAllies("Garrosh", "Thrall")).toBeTrue();
    });

    it("considers as allies if they are the same people", () => {
      const fm = anyManagerWithFactions([]);
      expect(fm.areAllies("Garrosh", "Garrosh")).toBeTrue();
    });

    it("does not mark two people as allies if they belong to separate factions", () => {
      const fm = anyManagerWithFactions(["Horde", "Alliance"]);

      fm.join("Horde", "Garrosh");
      fm.join("Alliance", "Anduin");

      expect(fm.areAllies("Garrosh", "Anduin")).toBeFalse();
    });

    it("does not mark two people as allies after one of the has left the faction", () => {
      const fm = anyManagerWithFactions(["Horde"]);
      fm.join("Horde", "Garrosh");
      fm.join("Horde", "Thrall");
      fm.leave("Horde", "Thrall");

      expect(fm.areAllies("Garrosh", "Thrall")).toBeFalse();
    });
  });
});
