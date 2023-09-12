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

factions.join("Alliance", warrior.name);
factions.join("Horde", goblin.name);

const areAllies = factions.areAllies(warrior.name, goblin.name);
const turns = [
  new AttackAction(goblin, warrior, areAllies),
  new HealingAction(warrior),
];

turns.forEach((x) => {
  x.run();
  console.log(`\t* ${warrior}`);
  console.log(`\t* ${goblin}`);
});
