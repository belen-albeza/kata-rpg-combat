interface CharacterOptions {
  health?: number;
}

const defaults = {
  health: 1000,
};

export default class Character {
  readonly health: number;

  constructor(options?: CharacterOptions) {
    const { health } = { ...defaults, ...options };

    this.health = health;
  }
}
