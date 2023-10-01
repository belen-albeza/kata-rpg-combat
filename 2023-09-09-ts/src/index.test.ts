import { describe, it, expect } from "vitest";

import Character from "./character";
import { AttackAction, HealingAction } from ".";

describe("AttackAction", () => {
  it("can deal damage to another character", () => {
    const c = new Character();
    c.attack = 20;
    const other = new Character();
    const attack = new AttackAction(c, other);

    attack.perform();

    expect(other.health).toBe(980);
  });

  it("kills other character when damage exceeds health", () => {
    const c = new Character();
    c.attack = 2000;
    const other = new Character();
    const attack = new AttackAction(c, other);

    attack.perform();

    expect(other.health).toBe(0);
    expect(other.isAlive).toBeFalsy();
  });

  it("cannot target themselves", () => {
    const c = new Character();
    const attack = new AttackAction(c, c);

    expect(() => attack.perform()).toThrowError(/themselves/);
    expect(c.health).toBe(1000);
  });

  describe("Damage modifiers", () => {
    it("increases damage by 50% when target is 5+ levels below", () => {
      const c = new Character();
      c.attack = 100;
      c.level = 6;
      const other = new Character();
      const attack = new AttackAction(c, other);

      attack.perform();

      expect(other.health).toBe(850);
    });

    it("reduces damage by 50% when target is 5+ levels above", () => {
      const c = new Character();
      c.attack = 100;
      const other = new Character();
      other.level = 6;
      const attack = new AttackAction(c, other);

      attack.perform();

      expect(other.health).toBe(950);
    });
  });
});

describe("HealingAction", () => {
  it("can heal themselves", () => {
    const c = new Character();
    c.healPower = 20;
    c.health = 900;

    const healing = new HealingAction(c, c);
    healing.perform();

    expect(c.health).toBe(920);
  });

  it("cannot heal over 1000 hp", () => {
    const c = new Character();

    const healing = new HealingAction(c, c);
    healing.perform();

    expect(c.health).toBe(1000);
  });

  it("cannot heal a dead character", () => {
    const c = new Character();
    const other = new Character();
    other.health = 0;

    const healing = new HealingAction(c, other);

    expect(other.isAlive).toBeFalsy();
    expect(() => healing.perform()).toThrowError(/dead character/);

    expect(other.isAlive).toBeFalsy();
    expect(other.health).toBe(0);
  });

  it("can only heal themselves", () => {
    const c = new Character();
    const other = new Character();
    other.health = 900;
    const healing = new HealingAction(c, other);

    expect(() => healing.perform()).toThrowError(/themselves/);
    expect(other.health).toBe(900);
  });
});
