import { InvalidTargetError } from "./index";
import { describe, it, expect } from "vitest";

import { Character, AttackAction, HealingAction } from ".";

describe("Character", () => {
  it("is initialized at level 1 and 1000 health", () => {
    const c = new Character();
    expect(c.health).toBe(1000);
    expect(c.level).toBe(1);
  });

  it("returns whether is alive or not", () => {
    const c = new Character();
    expect(c.isAlive).toBeTruthy();
    c.health = 0;
    expect(c.isAlive).toBeFalsy();
  });
});

describe("AttackAction", () => {
  it("can deal damage to another character", () => {
    const c = new Character();
    c.attack = 20;
    const other = new Character();
    const attack = new AttackAction(c, other);

    attack.perform();

    expect(other.health).toBe(980);
  });

  it("kills other character when damage exceeds health", () => {
    const c = new Character();
    c.attack = 2000;
    const other = new Character();
    const attack = new AttackAction(c, other);

    attack.perform();

    expect(other.health).toBe(0);
    expect(other.isAlive).toBeFalsy();
  });
});

describe("HealingAction", () => {
  it("can heal another character", () => {
    const c = new Character();
    c.healPower = 20;
    const other = new Character();
    other.health = 900;

    const healing = new HealingAction(c, other);
    healing.perform();

    expect(other.health).toBe(920);
  });

  it("cannot heal over 1000 hp", () => {
    const c = new Character();
    const other = new Character();

    const healing = new HealingAction(c, other);
    healing.perform();

    expect(other.health).toBe(1000);
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
});
