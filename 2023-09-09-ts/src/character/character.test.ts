import { describe, it, expect } from "vitest";

import Character, { MeleeFighter } from ".";

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

describe("MeleeFighter", () => {
  it("has an attack and range", () => {
    const c = new MeleeFighter(100);
    expect(c.attack).toBe(100);
    expect(c.attackRange).toBe(2);
  });
});
