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

interface HasLevel {
  level: number;
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
  protected readonly target: HasHealth & HasLevel;
  protected readonly source: T;

  constructor(source: T, target: HasHealth & HasLevel) {
    this.target = target;
    this.source = source;
  }

  abstract perform(): void;
}

export class AttackAction extends CombatAction<DamageDealer & HasLevel> {
  perform(): void {
    if (this.source === (this.target as unknown)) {
      throw new InvalidTargetError("a character cannot target themselves");
    }

    const levelDiff = this.source.level - this.target.level;
    const modifier = levelDiff >= 5 ? 1.5 : levelDiff <= -5 ? 0.5 : 1.0;

    this.target.health -= this.source.attack * modifier;
  }
}

export class HealingAction extends CombatAction<Healer> {
  perform(): void {
    if (!this.target.isAlive) {
      throw new InvalidTargetError("cannot heal dead characters");
    } else if (this.target !== (this.source as unknown)) {
      throw new InvalidTargetError("a character can only heal themselves");
    }

    this.target.health += this.source.healPower;
  }
}
