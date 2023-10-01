import { describe, it, expect } from "vitest";

import Character from "./character";
import { AttackAction, HealingAction } from "./actions";

describe("RPG Combat", () => {
  it("Characters kill other characters when the damage dealth exceeds health", () => {
    const c = new Character();
    c.attack = 2000;
    const other = new Character();
    const attack = new AttackAction(c, other);

    attack.perform();

    expect(other.health).toBe(0);
    expect(other.isAlive).toBeFalsy();
  });

  it("characters cannot heal over 1000 hp", () => {
    const c = new Character();
    c.healPower = 10;

    const healing = new HealingAction(c, c);
    healing.perform();

    expect(c.health).toBe(1000);
  });
});
