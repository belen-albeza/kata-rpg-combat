import type { WithHealth } from "../character";

export class InvalidTargetError extends Error {}

interface Attacker {
  attack: number;
}

interface AttackTarget extends WithHealth {}

export class AttackAction {
  readonly #source: Attacker;
  readonly #target: AttackTarget;

  constructor(source: Attacker, target: AttackTarget) {
    if (source === (target as unknown)) {
      throw new InvalidTargetError("attackers cannot target themselves");
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
