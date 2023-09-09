export class InvalidTargetError extends Error {}

interface HasHealth {
  health: number;
  get isAlive(): boolean;
}

interface DamageDealer {
  attack: number;
}

interface Healer {
  healPower: number;
}

export class Character implements HasHealth, DamageDealer, Healer {
  static readonly #MAX_HEALTH = 1000;

  #health: number = Character.#MAX_HEALTH;
  level: number = 1;
  attack: number = 10;
  healPower: number = 10;

  get health(): number {
    return this.#health;
  }

  set health(value: number) {
    this.#health = Math.min(Character.#MAX_HEALTH, Math.max(0, value));
  }

  get isAlive(): boolean {
    return this.health > 0;
  }
}

export class AttackAction {
  readonly #target: HasHealth;
  readonly #source: DamageDealer;

  constructor(source: DamageDealer, target: HasHealth) {
    this.#target = target;
    this.#source = source;
  }

  perform(): void {
    this.#target.health -= this.#source.attack;
  }
}

export class HealingAction {
  readonly #target: HasHealth;
  readonly #source: Healer;

  constructor(source: Healer, target: HasHealth) {
    this.#target = target;
    this.#source = source;
  }

  perform(): void {
    if (!this.#target.isAlive) {
      throw new InvalidTargetError("cannot heal dead characters");
    }

    this.#target.health += this.#source.healPower;
  }
}
