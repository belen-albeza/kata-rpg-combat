import { HasHealth, DamageDealer, Healer, HasLevel } from "./types";

interface Action {
  perform(): void;
}

export class InvalidTargetError extends Error {}

interface Attacker extends DamageDealer, HasLevel {}
export class AttackAction implements Action {
  #source: Attacker;
  #target: HasHealth & HasLevel;

  constructor(source: Attacker, target: HasHealth & HasLevel) {
    this.#source = source;
    this.#target = target;
  }

  perform(): void {
    if (this.#source === (this.#target as unknown)) {
      throw new InvalidTargetError("a character cannot target themselves");
    }

    const levelDiff = this.#source.level - this.#target.level;
    const modifier = levelDiff >= 5 ? 1.5 : levelDiff <= -5 ? 0.5 : 1.0;

    this.#target.health -= this.#source.attack * modifier;
  }
}

export class HealingAction implements Action {
  #source: Healer;
  #target: HasHealth;

  constructor(source: Healer, target: HasHealth) {
    this.#source = source;
    this.#target = target;
  }

  perform(): void {
    if (!this.#target.isAlive) {
      throw new InvalidTargetError("cannot heal dead characters");
    } else if (this.#target !== (this.#source as unknown)) {
      throw new InvalidTargetError("a character can only heal themselves");
    }

    this.#target.health += this.#source.healPower;
  }
}
