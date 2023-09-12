import type { WithHealth, WithLevel } from ".";
import { InvalidSourceError, InvalidTargetError } from ".";

interface Attacker extends WithHealth, WithLevel {
  attack: number;
}

interface AttackTarget extends WithHealth, WithLevel {}

export class AttackAction {
  readonly #source: Attacker;
  readonly #target: AttackTarget;

  constructor(source: Attacker, target: AttackTarget, areAllies: boolean) {
    if (source === target) {
      throw new InvalidTargetError("attackers cannot target themselves");
    } else if (!source.isAlive) {
      throw new InvalidSourceError("attackers cannot be dead");
    } else if (areAllies) {
      throw new InvalidTargetError("attackers cannot target allies");
    }

    this.#source = source;
    this.#target = target;
  }

  run() {
    console.log(
      `${this.#source} attacks ${this.#target} and deals ${this.#damage} damage`
    );

    this.#target.health -= this.#damage;
  }

  get #damage(): number {
    const levelDiff = this.#source.level - this.#target.level;
    const damageModifier = levelDiff >= 5 ? 1.5 : levelDiff <= -5 ? 0.5 : 1.0;

    return this.#source.attack * damageModifier;
  }
}
