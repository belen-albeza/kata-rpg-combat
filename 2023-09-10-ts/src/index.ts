import { CharacterBuilder } from "./character";
import { AttackAction } from "./combat/attack-action";

const warrior = new CharacterBuilder()
  .withName("Warrior")
  .withAttack(100)
  .withHealing(50)
  .build();
const goblin = new CharacterBuilder()
  .withName("Goblin")
  .withAttack(50)
  .withHealth(300)
  .build();

const turns = [new AttackAction(warrior, goblin)];

turns.forEach((x) => {
  x.run();
  console.log(`\t${warrior}`);
  console.log(`\t${goblin}`);
});
