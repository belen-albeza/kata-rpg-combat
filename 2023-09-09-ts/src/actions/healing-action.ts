import { WithHealing, HasHealth } from "./../types";
import { Action, InvalidTargetError } from ".";

export interface Healer extends WithHealing {}
export interface HealingTarget extends HasHealth {}

export class HealingAction implements Action {
  #source: WithHealing;
  #target: HealingTarget;

  constructor(source: WithHealing, target: HealingTarget) {
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
