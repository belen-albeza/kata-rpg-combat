export class InvalidTargetError extends Error {}

interface HasHealth {
  health: number;
  get isAlive(): boolean;
}

export class Character implements HasHealth {
  static readonly #MAX_HEALTH = 1000;

  #health: number = Character.#MAX_HEALTH;
  level: number = 1;

  get health(): number {
    return this.#health;
  }

  set health(value: number) {
    this.#health = Math.min(Character.#MAX_HEALTH, Math.max(0, value));
  }

  get isAlive(): boolean {
    return this.health > 0;
  }

  dealDamage(hp: number, target: HasHealth): void {
    target.health -= hp;
  }

  heal(hp: number, target: HasHealth): void {
    if (!target.isAlive) {
      throw new InvalidTargetError("Cannot heal dead characters");
    }

    target.health += hp;
  }
}
