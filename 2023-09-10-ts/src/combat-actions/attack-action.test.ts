import { describe, it, expect } from "bun:test";
import { AttackAction } from "./attack-action";

const anyAttackerWithDamage = (damage: number) => {
  return { attack: damage, health: 1000, isAlive: true };
};

const anyTargetWithHealth = (health: number) => {
  return { health, isAlive: health > 0 };
};

describe("AttackAction", () => {
  it("damages target", () => {
    const attacker = anyAttackerWithDamage(100);
    const target = anyTargetWithHealth(1000);

    const action = new AttackAction(attacker, target);
    action.run();

    expect(target.health).toBe(900);
  });

  it("throws when attacker is the same as target", () => {
    const attacker = anyAttackerWithDamage(100);

    expect(() => {
      const action = new AttackAction(attacker, attacker);
    }).toThrow(/cannot target themselves/);
  });

  it("throws when attacker is dead", () => {
    const attacker = Object.assign(
      {},
      anyAttackerWithDamage(100),
      anyTargetWithHealth(0)
    );

    const target = anyTargetWithHealth(1000);

    expect(() => {
      const action = new AttackAction(attacker, target);
    }).toThrow(/dead/);
  });
});
