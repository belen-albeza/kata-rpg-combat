export { AttackAction } from "./attack-action";
export { HealingAction } from "./healing-action";

export class InvalidTargetError extends Error {}

export interface Action {
  perform(): void;
}
