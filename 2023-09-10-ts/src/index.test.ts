import { describe, it, expect } from "bun:test";

import { AttackAction, HealingAction } from "./combat-actions";
import { CharacterBuilder } from "./character";

describe("RPG Combat", () => {
  it("has characters attacking other characters", () => {
    const attacker = new CharacterBuilder().withAttack(100).build();
    const target = new CharacterBuilder().withHealth(1000).build();
    const attack = new AttackAction(attacker, target);

    attack.run();

    expect(target.health).toBe(900);
  });

  it("has characters than can heal themselves", () => {
    const healer = new CharacterBuilder()
      .withHealing(100)
      .withHealth(800)
      .build();
    const healing = new HealingAction(healer);

    healing.run();

    expect(healer.health).toBe(900);
  });
});
