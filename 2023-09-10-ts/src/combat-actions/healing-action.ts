import { WithHealth, InvalidSourceError, InvalidTargetError } from ".";
interface Healer extends WithHealth {
  healing: number;
}

interface HealingTarget extends WithHealth {}

interface FactionInformer {
  areAllies(source: Healer, target: HealingTarget): boolean;
}

export class HealingAction {
  readonly #source: Healer;
  readonly #target: HealingTarget;

  constructor(
    source: Healer,
    target: HealingTarget,
    factions: FactionInformer
  ) {
    if (!source.isAlive) {
      throw new InvalidSourceError("healers cannot be dead");
    } else if (source !== target && !factions.areAllies(source, target)) {
      throw new InvalidTargetError(
        "healers can only heal allies or themselves"
      );
    } else if (!target.isAlive) {
      throw new InvalidTargetError("healers cannot heal dead characters");
    }

    this.#source = source;
    this.#target = target;
  }

  run() {
    const hp = this.#source.healing;

    console.log(`${this.#source} heals for ${hp} HP`);

    this.#target.health += hp;
  }
}
