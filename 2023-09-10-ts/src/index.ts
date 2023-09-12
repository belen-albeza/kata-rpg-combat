import { CharacterBuilder } from "./character";
import { AttackAction, HealingAction } from "./combat-actions";
import { FactionManager } from "./factions";

const factions = new FactionManager();
factions.add("Horde");
factions.add("Alliance");

const warrior = new CharacterBuilder()
  .withName("Elven Warrior")
  .withAttack(100)
  .withHealing(50)
  .build();
const goblin = new CharacterBuilder()
  .withName("Goblin")
  .withAttack(80)
  .withHealth(300)
  .build();

factions.join("Alliance", warrior);
factions.join("Horde", goblin);

const turns = [
  new AttackAction(goblin, warrior, factions),
  new HealingAction(warrior),
];

turns.forEach((x) => {
  x.run();
  console.log(`\t* ${warrior}`);
  console.log(`\t* ${goblin}`);
});
