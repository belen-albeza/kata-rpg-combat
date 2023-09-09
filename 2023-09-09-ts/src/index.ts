export class Character {
  static readonly #MAX_HEALTH = 1000;

  health: number = Character.#MAX_HEALTH;
  level: number = 1;

  get isAlive(): boolean {
    return this.health > 0;
  }

  dealDamage(hp: number, target: Character): void {
    target.health = Math.max(0, target.health - hp);
  }

  heal(hp: number, target: Character): void {
    if (!target.isAlive) {
      return;
    }

    target.health = Math.min(Character.#MAX_HEALTH, target.health + hp);
  }
}
