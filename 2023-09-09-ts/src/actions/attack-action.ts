import { DamageDealer, HasLevel, HasHealth, HasPosition } from "../types";
import { Action, InvalidTargetError } from ".";

export interface Attacker extends DamageDealer, HasLevel, HasPosition {}
export interface AttackTarget extends HasHealth, HasLevel, HasPosition {}

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
    if (!this.#isTargetInRange) {
      throw new InvalidTargetError("target is out of range");
    }

    const levelDiff = this.#source.level - this.#target.level;
    const modifier = levelDiff >= 5 ? 1.5 : levelDiff <= -5 ? 0.5 : 1.0;

    this.#target.health -= this.#source.attack * modifier;
  }

  get #isTargetInRange(): boolean {
    const distance = Math.sqrt(
      Math.pow(this.#target.position.x - this.#source.position.x, 2) +
        Math.pow(this.#target.position.y - this.#source.position.y, 2)
    );

    return distance <= this.#source.attackRange;
  }
}
