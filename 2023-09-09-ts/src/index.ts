import { AttackAction, HealingAction } from "./actions";
import Character from "./character";

const elf = new Character();
elf.attack = 100;
elf.healPower = 50;

const goblin = new Character();
goblin.attack = 80;

const turns = [
  new AttackAction(elf, goblin),
  new AttackAction(goblin, elf),
  new HealingAction(elf, elf),
];

for (const turn of turns) {
  turn.perform();
}

console.log(`Combat ended! Elf now has ${elf.health} HP`);
