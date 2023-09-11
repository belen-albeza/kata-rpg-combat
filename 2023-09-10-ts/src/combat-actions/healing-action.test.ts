import { describe, it, expect } from "bun:test";
import { HealingAction } from "./healing-action";

const anyHealerWithHealing = (healing: number) => {
  return { healing, health: 1000, isAlive: true };
};

const anyTargetWithHealth = (health: number) => {
  return { health, isAlive: health > 0 };
};

describe("HealingAction", () => {
  it("heals themselves", () => {
    const healer = Object.assign(
      {},
      anyHealerWithHealing(100),
      anyTargetWithHealth(800)
    );

    const action = new HealingAction(healer);
    action.run();

    expect(healer.health).toBe(900);
  });

  it("throws when healer is dead", () => {
    const healer = Object.assign(
      {},
      anyHealerWithHealing(100),
      anyTargetWithHealth(0)
    );

    const target = anyTargetWithHealth(1000);

    expect(() => {
      const action = new HealingAction(healer);
    }).toThrow(/dead/);
  });
});
