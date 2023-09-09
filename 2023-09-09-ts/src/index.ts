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
  level: number = 1;
  attack: number = 10;
  healPower: number = 10;

  #health: Health = new Health();

  get health(): number {
    return this.#health.value;
  }

  set health(value: number) {
    this.#health.value = value;
  }

  get isAlive(): boolean {
    return this.#health.isAlive;
  }
}

class Health {
  static readonly #MAX_HEALTH = 1000;
  #value: number = Health.#MAX_HEALTH;

  get value(): number {
    return this.#value;
  }

  set value(value: number) {
    this.#value = Math.min(Health.#MAX_HEALTH, Math.max(0, value));
  }

  get isAlive(): boolean {
    return this.value > 0;
  }
}

abstract class CombatAction<T> {
  protected readonly target: HasHealth;
  protected readonly source: T;

  constructor(source: T, target: HasHealth) {
    this.target = target;
    this.source = source;
  }

  abstract perform(): void;
}

export class AttackAction extends CombatAction<DamageDealer> {
  perform(): void {
    this.target.health -= this.source.attack;
  }
}

export class HealingAction extends CombatAction<Healer> {
  perform(): void {
    if (!this.target.isAlive) {
      throw new InvalidTargetError("cannot heal dead characters");
    }

    this.target.health += this.source.healPower;
  }
}
