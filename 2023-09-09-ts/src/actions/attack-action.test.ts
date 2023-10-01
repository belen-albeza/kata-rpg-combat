import { describe, it, expect } from "vitest";

import { AttackAction } from ".";
import { Attacker, AttackTarget } from "./attack-action";

const anyAttackerWithAttackAndLevel: (
  attack: number,
  level: number
) => Attacker & AttackTarget = (attack, level) => ({
  attack,
  level,
  health: 1000,
  isAlive: true,
});

const anyAttackerWithAttack = (attack: number) => {
  return anyAttackerWithAttackAndLevel(attack, 1);
};

const anyAttacker = () => {
  return anyAttackerWithAttackAndLevel(0, 1);
};

const anyTargetWithHealthAndLevel: (
  health: number,
  level: number
) => AttackTarget = (health, level) => {
  return {
    health,
    get isAlive() {
      return this.health > 0;
    },
    level,
  };
};

const anyTargetWithHealth = (health: number) => {
  return anyTargetWithHealthAndLevel(health, 1);
};

describe("AttackAction", () => {
  it("can deal damage to another character", () => {
    const c = anyAttackerWithAttack(20);
    const other = anyTargetWithHealth(1000);
    const attack = new AttackAction(c, other);

    attack.perform();

    expect(other.health).toBe(980);
  });

  it.todo("kills other character when damage exceeds health", () => {
    const c = anyAttackerWithAttack(2000);
    const other = anyTargetWithHealth(1000);
    const attack = new AttackAction(c, other);

    attack.perform();

    expect(other.health).toBe(0);
    expect(other.isAlive).toBeFalsy();
  });

  it("cannot target themselves", () => {
    const c = anyAttacker();
    const attack = new AttackAction(c, c);

    expect(() => attack.perform()).toThrowError(/themselves/);
    expect(c.health).toBe(1000);
  });

  describe("Damage modifiers", () => {
    it("increases damage by 50% when target is 5+ levels below", () => {
      const c = anyAttackerWithAttackAndLevel(100, 6);
      const other = anyTargetWithHealthAndLevel(1000, 1);
      const attack = new AttackAction(c, other);

      attack.perform();

      expect(other.health).toBe(850);
    });

    it("reduces damage by 50% when target is 5+ levels above", () => {
      const c = anyAttackerWithAttackAndLevel(100, 1);
      const other = anyTargetWithHealthAndLevel(1000, 6);
      other.level = 6;
      const attack = new AttackAction(c, other);

      attack.perform();

      expect(other.health).toBe(950);
    });
  });
});
