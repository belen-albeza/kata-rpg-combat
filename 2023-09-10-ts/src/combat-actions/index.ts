export class InvalidTargetError extends Error {}
export class InvalidSourceError extends Error {}
export interface WithHealth {
  health: number;
  isAlive: boolean;
}
export interface WithLevel {
  level: number;
}

export { AttackAction } from "./attack-action";
export { HealingAction } from "./healing-action";
