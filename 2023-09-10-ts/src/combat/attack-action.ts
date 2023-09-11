import type { WithHealth } from "../character";
import { InvalidSourceError, InvalidTargetError } from ".";

interface Attacker extends WithHealth {
  attack: number;
}

interface AttackTarget extends WithHealth {}

export class AttackAction {
  readonly #source: Attacker;
  readonly #target: AttackTarget;

  constructor(source: Attacker, target: AttackTarget) {
    if (source === target) {
      throw new InvalidTargetError("attackers cannot target themselves");
    } else if (!source.isAlive) {
      throw new InvalidSourceError("attackers cannot be dead");
    }

    this.#source = source;
    this.#target = target;
  }

  run() {
    const damage = this.#source.attack;

    console.log(
      `${this.#source} attacks ${this.#target} and deals ${damage} damage`
    );

    this.#target.health -= damage;
  }
}
