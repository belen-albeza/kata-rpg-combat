import { describe, it, expect } from "vitest";

import Character from ".";

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
