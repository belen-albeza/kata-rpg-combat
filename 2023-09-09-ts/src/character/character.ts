import { HasHealth, DamageDealer, Healer, HasLevel } from "../types";

export class Character implements HasLevel, HasHealth, DamageDealer, Healer {
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
