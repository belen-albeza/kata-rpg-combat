# RPG Combat kata (Rust version)

Original source: [RPG Combat kata by Daniel Ojeda](https://www.slideshare.net/DanielOjedaLoisel/rpg-combat-kata)

> [!TLDR]
>
> - A kata by iterations. New features will be added in each iteration.
> - For the scope of this kata, the player won't be moving around and doing stuff. The kata is centered around the combat system.

## Instructions

### Iteration #1

**Characters** have:

- Health, starting at `1000`.
- Level, starting at `1`.

Characters are:

- Dead or alive

Characters can:

- Attack (deal damage)
- Heal damage

Conditions:

- When the damaged received is higher than the actual health, health drops to `0` and the character dies.
- When the character is dead, they cannot be healed.
- Characters cannot be healed over `1000` health.

### Iteration #2

**Attack**:

- A character can deal damage to their enemies, but not to themselves.

**Healing**:

- A character can heal themselves, but not their enemies.

The **level** now has an effect on the damage dealt when attacking:

- If the target is `5` or more levels above the attacking character, the damage will be reduced by 50%.
- If the target is `5` or more levels below the attacking character, the damage will be boosted by 50%.

### Iteration #3

Characters have an **attack range**:

- For melee fighters, their range is 2 meters.
- For ranged fighters, their range is 20 meters.
- When trying to attack, their target must be in range.

### Iteration #4

We now have **factions**.

- A character may join or leave one or more factions.

Characters from the same faction are **allies**.

- Allies can't damage each other.
- Allies can heal each other.

### Iteration #5

Characters can damage other things that are not characters (**props**). This means they can attack a house, a tree or anything that has some health.

- Props cannot heal nor be healed, and cannot deal damage.
- Props belong to no faction, they are neutral.
