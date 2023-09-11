import { describe, it, expect } from "bun:test";

import { AttackAction } from "./combat/attack-action";
import { Character, CharacterBuilder } from "./character";

describe("RPG Combat", () => {
  it("has characters attacking other characters", () => {
    const attacker = new CharacterBuilder().withAttack(100).build();
    const target = new CharacterBuilder().withHealth(1000).build();

    const attack = new AttackAction(attacker, target);
    attack.run();

    expect(target.health).toBe(900);
  });
});
