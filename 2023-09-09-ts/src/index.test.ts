import { describe, it, expect } from "vitest";

import Character from "./character";
import { AttackAction } from "./actions";

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
});
