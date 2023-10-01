import {
  HasHealth,
  DamageDealer,
  WithHealing,
  HasLevel,
  Position,
  HasPosition,
} from "../types";

export class Character
  implements HasLevel, HasHealth, WithHealing, HasPosition
{
  level: number = 1;
  healPower: number = 10;

  #health: Health = new Health();
  position: Position = { x: 0, y: 0 };

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

export class MeleeFighter extends Character implements DamageDealer {
  attack: number;

  constructor(attack: number) {
    super();
    this.attack = attack;
  }

  get attackRange(): number {
    return 2;
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
