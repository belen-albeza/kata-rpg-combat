export interface HasHealth {
  health: number;
  get isAlive(): boolean;
}

export interface DamageDealer {
  attack: number;
  attackRange: number;
}

export interface WithHealing {
  healPower: number;
}

export interface HasLevel {
  level: number;
}
