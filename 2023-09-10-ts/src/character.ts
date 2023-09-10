export class InvalidSourceError extends Error {}
export class InvalidTargetError extends Error {}

export class Character {
  static readonly #MAX_HEALTH = 1000;

  health: number = Character.#MAX_HEALTH;
  attack: number = 0;
  healing: number = 0;

  get isAlive(): boolean {
    return this.health > 0;
  }

  dealDamage(other: Character) {
    if (other === this) {
      throw new InvalidTargetError(
        "character cannot target themselves for attack"
      );
    }

    other.health = Math.max(0, other.health - this.attack);
  }

  healDamage() {
    if (!this.isAlive) {
      throw new InvalidSourceError("character is dead");
    }

    this.health = Math.min(this.health + this.healing, Character.#MAX_HEALTH);
  }
}
