import { describe, it, expect, mock } from "bun:test";
import { AttackAction } from "./attack-action";

const anyAttackerWithDamage = (damage: number) => {
  return anyAttackerWithDamageAndLevel(damage, 1);
};

const anyTargetWithHealth = (health: number) => {
  return anyTargetWithHealthAndLevel(health, 1);
};

const anyAttackerWithDamageAndLevel = (damage: number, level: number) => {
  return { attack: damage, health: 1000, isAlive: true, level };
};

const anyTargetWithHealthAndLevel = (health: number, level: number) => {
  return { health, isAlive: health > 0, level };
};

const anyFactionManager = () => {
  return {
    areAllies: () => false,
  };
};

describe("AttackAction", () => {
  it("damages target", () => {
    const attacker = anyAttackerWithDamage(100);
    const target = anyTargetWithHealth(1000);

    const action = new AttackAction(attacker, target, anyFactionManager());
    action.run();

    expect(target.health).toBe(900);
  });

  it("deals 150% damage when target is 5+ levels below the attacker", () => {
    const attacker = anyAttackerWithDamageAndLevel(100, 6);
    const target = anyTargetWithHealthAndLevel(1000, 1);

    const action = new AttackAction(attacker, target, anyFactionManager());
    action.run();

    expect(target.health).toBe(850);
  });

  it("deals 50% damage when target is 5+ levels above the attacker", () => {
    const attacker = anyAttackerWithDamageAndLevel(100, 1);
    const target = anyTargetWithHealthAndLevel(1000, 6);

    const action = new AttackAction(attacker, target, anyFactionManager());
    action.run();

    expect(target.health).toBe(950);
  });

  it("throws when attacker is the same as target", () => {
    const attacker = anyAttackerWithDamage(100);

    expect(() => {
      const action = new AttackAction(attacker, attacker, anyFactionManager());
    }).toThrow(/cannot target themselves/);
  });

  it("throws when attacker and target are allies", () => {
    const attacker = anyAttackerWithDamage(100);
    const target = anyTargetWithHealth(1000);

    const factions = anyFactionManager();
    factions.areAllies = mock(() => true);

    expect(() => {
      const action = new AttackAction(attacker, target, factions);
    }).toThrow(/cannot target allies/);
  });

  it("throws when attacker is dead", () => {
    const attacker = Object.assign(
      {},
      anyAttackerWithDamage(100),
      anyTargetWithHealth(0)
    );

    const target = anyTargetWithHealth(1000);

    expect(() => {
      const action = new AttackAction(attacker, target, anyFactionManager());
    }).toThrow(/dead/);
  });
});
