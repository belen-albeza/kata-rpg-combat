import { DamageDealer, HasLevel, HasHealth } from "../types";
import { Action, InvalidTargetError } from ".";

export interface Attacker extends DamageDealer, HasLevel {}
export interface AttackTarget extends HasHealth, HasLevel {}

export class AttackAction implements Action {
  #source: Attacker;
  #target: AttackTarget;

  constructor(source: Attacker, target: AttackTarget) {
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
