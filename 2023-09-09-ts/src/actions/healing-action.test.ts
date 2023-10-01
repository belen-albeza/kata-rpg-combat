import { describe, it, expect } from "vitest";

import { HealingAction } from ".";
import Character from "../character";

describe("HealingAction", () => {
  it("can heal themselves", () => {
    const c = new Character();
    c.healPower = 20;
    c.health = 900;

    const healing = new HealingAction(c, c);
    healing.perform();

    expect(c.health).toBe(920);
  });

  it("cannot heal over 1000 hp", () => {
    const c = new Character();

    const healing = new HealingAction(c, c);
    healing.perform();

    expect(c.health).toBe(1000);
  });

  it("cannot heal a dead character", () => {
    const c = new Character();
    const other = new Character();
    other.health = 0;

    const healing = new HealingAction(c, other);

    expect(other.isAlive).toBeFalsy();
    expect(() => healing.perform()).toThrowError(/dead character/);

    expect(other.isAlive).toBeFalsy();
    expect(other.health).toBe(0);
  });

  it("can only heal themselves", () => {
    const c = new Character();
    const other = new Character();
    other.health = 900;
    const healing = new HealingAction(c, other);

    expect(() => healing.perform()).toThrowError(/themselves/);
    expect(other.health).toBe(900);
  });
});
