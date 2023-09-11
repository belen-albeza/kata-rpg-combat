import { describe, it, expect } from "bun:test";
import { Character } from "./character";

class CharacterBuilder {
  #character: Character = new Character();

  build(): Character {
    return this.#character;
  }

  withHealth(health: number) {
    this.#character.health = health;
    return this;
  }

  withAttack(attack: number) {
    this.#character.attack = attack;
    return this;
  }

  withHealing(healing: number) {
    this.#character.healing = healing;
    return this;
  }
}

describe("Character", () => {
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

  describe("Attacking", () => {
    it("can damage another character", () => {
      const c = new CharacterBuilder().withAttack(100).build();
      const other = new CharacterBuilder().withHealth(1000).build();

      c.dealDamage(other);

      expect(other.health).toBe(900);
    });

    it("cannot damage themselves", () => {
      const c = new CharacterBuilder().withAttack(100).withHealth(1000).build();

      expect(() => {
        c.dealDamage(c);
      }).toThrow(/cannot target themselves/);
      expect(c.health).toBe(1000);
    });

    it("cannot reduce health below zero", () => {
      const c = new CharacterBuilder().withAttack(100).build();
      const other = new CharacterBuilder().withHealth(1).build();

      c.dealDamage(other);

      expect(other.health).toBe(0);
      expect(other.isAlive).toBeFalse();
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
