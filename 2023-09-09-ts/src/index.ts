import { AttackAction, HealingAction } from "./actions";
import { MeleeFighter, RangedFighter } from "./character";

const elf = new MeleeFighter(100);
elf.healPower = 50;

const goblin = new RangedFighter(80);

const turns = [
  new AttackAction(elf, goblin),
  new AttackAction(goblin, elf),
  new HealingAction(elf, elf),
];

for (const turn of turns) {
  turn.perform();
}

console.log(`Combat ended! Elf now has ${elf.health} HP`);
