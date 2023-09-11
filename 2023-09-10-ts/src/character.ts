export interface WithHealth {
  health: number;
  isAlive: boolean;
}

export class Character implements WithHealth {
  static readonly #MAX_HEALTH = 1000;

  #health: number = Character.#MAX_HEALTH;
  attack: number = 0;
  healing: number = 0;
  readonly name: string;

  constructor(name: string = "Anonymous") {
    this.name = name;
  }

  get health(): number {
    return this.#health;
  }

  set health(value: number) {
    this.#health = Math.min(Math.max(0, value), Character.#MAX_HEALTH);
  }

  get isAlive(): boolean {
    return this.health > 0;
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
