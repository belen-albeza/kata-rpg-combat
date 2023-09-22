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

    it("cannot have their health raised above the max health limit", () => {
      const c = new Character();
      c.health = 1001;
      expect(c.health).toBe(1000);
    });

    it("raises max health limit to 1500 at level 6 and above", () => {
      const c = new CharacterBuilder().withLevel(6).withHealth(1000).build();

      c.health += 1000;

      expect(c.health).toBe(1500);
    });
  });

  describe("Attributes", () => {
    describe("Level", () => {
      it("is created with level 1 as default", () => {
        const c = new Character();
        expect(c.level).toBe(1);
      });

      it("cannot have their level set below 1", () => {
        const c = new Character();
        c.level = 0;
        expect(c.level).toBe(1);
      });
    });

    it("can deal damage", () => {
      const c = new CharacterBuilder().withAttack(100).build();
      expect(c.attack).toBe(100);
    });

    it("can heal", () => {
      const c = new CharacterBuilder().withHealing(100).build();
      expect(c.healing).toBe(100);
    });
  });
});
