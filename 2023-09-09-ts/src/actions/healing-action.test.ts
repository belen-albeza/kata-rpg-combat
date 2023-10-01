import { describe, it, expect } from "vitest";

import { HealingAction } from ".";
import { HealingTarget, Healer } from "./healing-action";

const anyHealerWithHealingAndHealth: (
  healPower: number,
  health: number
) => Healer & HealingTarget = (healPower, health) => {
  const target = anyTargetWithHealth(health);
  const healer = { healPower };
  return { ...healer, ...target };
};

const anyTargetWithHealth: (health: number) => HealingTarget = (health) => ({
  health,
  isAlive: health > 0,
});

describe("HealingAction", () => {
  it("can heal themselves", () => {
    const c = anyHealerWithHealingAndHealth(20, 900);

    const healing = new HealingAction(c, c);
    healing.perform();

    expect(c.health).toBe(920);
  });

  it.todo("cannot heal over 1000 hp", () => {
    const c = anyHealerWithHealingAndHealth(1, 1000);

    const healing = new HealingAction(c, c);
    healing.perform();

    expect(c.health).toBe(1000);
  });

  it("cannot heal a dead character", () => {
    const c = anyHealerWithHealingAndHealth(10, 1000);
    const other = anyTargetWithHealth(0);

    const healing = new HealingAction(c, other);

    expect(other.isAlive).toBeFalsy();
    expect(() => healing.perform()).toThrowError(/dead character/);
    expect(other.health).toBe(0);
  });

  it("can only heal themselves", () => {
    const c = anyHealerWithHealingAndHealth(10, 1000);
    const other = anyTargetWithHealth(900);

    const healing = new HealingAction(c, other);

    expect(() => healing.perform()).toThrowError(/themselves/);
    expect(other.health).toBe(900);
  });
});
