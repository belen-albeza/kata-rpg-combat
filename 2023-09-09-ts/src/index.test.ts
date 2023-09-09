import { describe, it, expect } from "vitest";

import { Character } from ".";

describe("Character", () => {
  it("is initialized at level 1 and 1000 health", () => {
    const c = new Character();
    expect(c.health).toBe(1000);
    expect(c.level).toBe(1);
  });

  it("returns whether is alive or not", () => {
    const c = new Character();
    expect(c.isAlive).toBeTruthy();
    c.health = 0;
    expect(c.isAlive).toBeFalsy();
  });

  describe("attack", () => {
    it("can deal damage to another character", () => {
      const c = new Character();
      const other = new Character();

      c.dealDamage(20, other);

      expect(other.health).toBe(980);
    });

    it("kills other character when damage exceeds health", () => {
      const c = new Character();
      const other = new Character();

      c.dealDamage(2000, other);

      expect(other.health).toBe(0);
      expect(other.isAlive).toBeFalsy();
    });
  });

  describe("healing", () => {
    it("can heal another character", () => {
      const c = new Character();
      const other = new Character();
      other.health = 900;

      c.heal(20, other);

      expect(other.health).toBe(920);
    });

    it("cannot heal over 1000 hp", () => {
      const c = new Character();
      const other = new Character();

      c.heal(20, other);

      expect(other.health).toBe(1000);
    });

    it("cannot heal a dead character", () => {
      const c = new Character();
      const other = new Character();
      other.health = 0;

      expect(other.isAlive).toBeFalsy();

      c.heal(1, other);

      expect(other.isAlive).toBeFalsy();
      expect(other.health).toBe(0);
    });
  });
});
