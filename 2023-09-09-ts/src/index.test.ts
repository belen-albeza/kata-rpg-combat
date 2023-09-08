import { describe, it, expect } from "vitest";

import { add } from ".";

describe("add", () => {
  it("adds two numbers", () => {
    expect(add(2, 2)).toBe(4);
  });
});
