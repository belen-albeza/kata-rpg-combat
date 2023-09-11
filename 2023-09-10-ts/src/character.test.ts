import { describe, it, expect } from "bun:test";
import { Character, CharacterBuilder } from "./character";

describe("Character", () => {
  describe("Health", () => {
    it("is created with 1000 health", () => {
      const c = new Character();
      expect(c.health).toBe(1000);
    });

    it("returns whether they are alive", () => {
      const alive = new CharacterBuilder().withHealth(1).build();
      const dead = new CharacterBuilder().withHealth(0).build();

      expect(alive.isAlive).toBeTrue();
      expect(dead.isAlive).toBeFalse();
    });

    it("cannot have their health reduced below zero", () => {
      const c = new Character();
      c.health = -1;
      expect(c.health).toBe(0);
    });

    it("cannot have their health raised above initial value", () => {
      const c = new Character();
      c.health = 1001;
      expect(c.health).toBe(1000);
    });
  });

  describe("Attributes", () => {
    it("can deal damage", () => {
      const c = new CharacterBuilder().withAttack(100).build();
      expect(c.attack).toBe(100);
    });

    it("can heal", () => {
      const c = new CharacterBuilder().withHealing(100).build();
      expect(c.healing).toBe(100);
    });
  });

  describe("Healing", () => {
    it("can heal themselves", () => {
      const c = new CharacterBuilder().withHealth(100).withHealing(200).build();
      c.healDamage();

      expect(c.health).toBe(300);
    });

    it("cannot heal over 1000 hp", () => {
      const c = new CharacterBuilder().withHealth(900).withHealing(200).build();

      c.healDamage();

      expect(c.health).toBe(1000);
    });

    it("cannot heal if they are dead", () => {
      const c = new CharacterBuilder().withHealth(0).withHealing(100).build();

      expect(() => {
        c.healDamage();
      }).toThrow(/dead/);

      expect(c.health).toBe(0);
    });
  });
});
