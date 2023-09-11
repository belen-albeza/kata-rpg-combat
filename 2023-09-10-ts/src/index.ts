import { CharacterBuilder } from "./character";
import { AttackAction, HealingAction } from "./combat-actions";

const warrior = new CharacterBuilder()
  .withName("Warrior")
  .withAttack(100)
  .withHealing(50)
  .build();
const goblin = new CharacterBuilder()
  .withName("Goblin")
  .withAttack(80)
  .withHealth(300)
  .build();

const turns = [new AttackAction(goblin, warrior), new HealingAction(warrior)];

turns.forEach((x) => {
  x.run();
  console.log(`\t* ${warrior}`);
  console.log(`\t* ${goblin}`);
});
