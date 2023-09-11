export class InvalidSourceError extends Error {}
export class InvalidTargetError extends Error {}

interface WithHealth {
  health: number;
  isAlive: boolean;
}

export class Character implements WithHealth {
  static readonly #MAX_HEALTH = 1000;

  #health: number = Character.#MAX_HEALTH;
  attack: number = 0;
  healing: number = 0;

  get health(): number {
    return this.#health;
  }

  set health(value: number) {
    this.#health = Math.min(Math.max(0, value), Character.#MAX_HEALTH);
  }

  get isAlive(): boolean {
    return this.health > 0;
  }

  dealDamage(other: WithHealth) {
    if (other === this) {
      throw new InvalidTargetError(
        "character cannot target themselves for attack"
      );
    }

    other.health -= this.attack;
  }

  healDamage() {
    if (!this.isAlive) {
      throw new InvalidSourceError("character is dead");
    }

    this.health += this.healing;
  }
}
