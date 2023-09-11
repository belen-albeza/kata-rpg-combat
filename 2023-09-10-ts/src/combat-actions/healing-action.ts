import { InvalidSourceError } from ".";
import type { WithHealth } from ".";

interface Healer extends WithHealth {
  healing: number;
}

export class HealingAction {
  readonly #source: Healer;

  constructor(source: Healer) {
    if (!source.isAlive) {
      throw new InvalidSourceError("healers cannot be dead");
    }

    this.#source = source;
  }

  run() {
    const hp = this.#source.healing;

    console.log(`${this.#source} heals for ${hp} HP`);

    this.#source.health += hp;
  }
}
