import type { WithHealth } from "./combat-actions";

export class Character implements WithHealth {
  static readonly #MAX_HEALTH_LOW_LEVELS = 1000;
  static readonly #MAX_HEALTH_HIGH_LEVELS = 1500;

  readonly name: string;

  #level: number = 1;
  #health: number = 0;
  attack: number = 0;
  healing: number = 0;

  constructor(name: string = "Anonymous") {
    this.name = name;
    this.#health = this.maxHealth;
  }

  get level(): number {
    return this.#level;
  }

  set level(value: number) {
    this.#level = Math.max(1, value);
  }

  get health(): number {
    return this.#health;
  }

  set health(value: number) {
    this.#health = Math.min(Math.max(0, value), this.maxHealth);
  }

  get isAlive(): boolean {
    return this.health > 0;
  }

  private get maxHealth(): number {
    return this.level >= 6
      ? Character.#MAX_HEALTH_HIGH_LEVELS
      : Character.#MAX_HEALTH_LOW_LEVELS;
  }

  toString() {
    return `${this.name} (${this.health} HP)`;
  }
}

export class CharacterBuilder {
  #character: Character = new Character();

  build(): Character {
    return this.#character;
  }

  withName(name: string) {
    const c = new Character(name);
    c.attack = this.#character.attack;
    c.healing = this.#character.healing;
    c.health = this.#character.health;

    this.#character = c;

    return this;
  }

  withLevel(level: number) {
    this.#character.level = 6;
    return this;
  }

  withHealth(health: number) {
    this.#character.health = health;
    return this;
  }

  withAttack(attack: number) {
    this.#character.attack = attack;
    return this;
  }

  withHealing(healing: number) {
    this.#character.healing = healing;
    return this;
  }
}
