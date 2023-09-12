import { describe, it, expect, mock } from "bun:test";
import { HealingAction } from "./healing-action";

const anyHealerWithHealing = (healing: number) => {
  return { healing, health: 1000, isAlive: true };
};

const anyTargetWithHealth = (health: number) => {
  return { health, isAlive: health > 0 };
};

const anyFactionManager = () => {
  return { areAllies: mock(() => false) };
};

describe("HealingAction", () => {
  it("heals themselves", () => {
    const healer = Object.assign(
      {},
      anyHealerWithHealing(100),
      anyTargetWithHealth(800)
    );

    const action = new HealingAction(healer, healer, anyFactionManager());
    action.run();

    expect(healer.health).toBe(900);
  });

  it("can heal an ally", () => {
    const healer = anyHealerWithHealing(100);
    const target = anyTargetWithHealth(800);
    const factions = anyFactionManager();
    factions.areAllies = mock(() => true);

    const action = new HealingAction(healer, target, factions);
    action.run();

    expect(target.health).toBe(900);
  });

  it("throws when healer is dead", () => {
    const healer = Object.assign(
      {},
      anyHealerWithHealing(100),
      anyTargetWithHealth(0)
    );

    const target = anyTargetWithHealth(1000);

    expect(() => {
      const action = new HealingAction(healer, healer, anyFactionManager());
    }).toThrow(/dead/);
  });

  it("throws when target is dead", () => {
    const healer = anyHealerWithHealing(100);
    const target = anyTargetWithHealth(0);
    const factions = anyFactionManager();
    factions.areAllies = mock(() => true);

    expect(() => {
      const action = new HealingAction(healer, target, factions);
    }).toThrow(/dead/);
  });

  it("throws when target is not an ally", () => {
    const healer = anyHealerWithHealing(100);
    const target = anyTargetWithHealth(0);
    const factions = anyFactionManager();

    expect(() => {
      const action = new HealingAction(healer, target, factions);
    }).toThrow(/allies/);
  });
});
