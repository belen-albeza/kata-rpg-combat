import { describe, it, expect } from "bun:test";

import { FactionManager } from "./factions";
import { AttackAction, HealingAction } from "./combat-actions";
import { CharacterBuilder } from "./character";

describe("RPG Combat", () => {
  it("has characters attacking other characters", () => {
    const factions = new FactionManager();
    const attacker = new CharacterBuilder().withAttack(100).build();
    const target = new CharacterBuilder().withHealth(1000).build();
    const attack = new AttackAction(attacker, target, factions);

    attack.run();

    expect(target.health).toBe(900);
  });

  it("has characters than can heal themselves", () => {
    const factions = new FactionManager();
    const healer = new CharacterBuilder()
      .withHealing(100)
      .withHealth(800)
      .build();
    const healing = new HealingAction(healer, healer, factions);

    healing.run();

    expect(healer.health).toBe(900);
  });

  it("has characters joining factions", () => {
    const factions = new FactionManager();
    const orc = new CharacterBuilder().build();
    const troll = new CharacterBuilder().build();
    const human = new CharacterBuilder().build();

    factions.add("Horde");
    factions.add("Alliance");
    factions.join("Horde", orc);
    factions.join("Horde", troll);
    factions.join("Alliance", human);

    expect(factions.areAllies(orc, troll)).toBeTrue();
    expect(factions.areAllies(orc, human)).toBeFalse();
  });
});
