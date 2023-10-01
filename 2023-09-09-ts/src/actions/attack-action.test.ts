import { describe, it, expect } from "vitest";

import { AttackAction } from ".";
import Character from "../character";

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
