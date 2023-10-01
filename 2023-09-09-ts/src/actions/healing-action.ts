import { Healer, HasHealth } from "./../types";
import { Action, InvalidTargetError } from ".";

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
