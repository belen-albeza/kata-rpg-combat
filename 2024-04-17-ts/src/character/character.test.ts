import { it, describe, expect } from "bun:test";

import Character from ".";

describe("Character", () => {
  it("starts with 1000 health", () => {
    let c = new Character();
    expect(c.health).toBe(1000);
  });

  it("is created with provided values", () => {
    let c = new Character({ health: 500 });
    expect(c.health).toBe(500);
  });
});
