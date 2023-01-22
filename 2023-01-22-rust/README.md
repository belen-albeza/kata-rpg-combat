# RPG Combat kata (Rust version)

Original source: [RPG Combat kata by Daniel Ojeda](https://www.slideshare.net/DanielOjedaLoisel/rpg-combat-kata)

> [!TLDR]
>
> - A kata by iterations. New features will be added in each iteration.
> - For the scope of this kata, the player won't be moving around and doing stuff. The kata is centered around the combat system.

## Instructions

### Iteration #1

Characters have:

- Health, starting at `1000`.
- Level, starting at `1`.

Characters are:

- Dead or alive

Characters can:

- Attack (deal damage)
- Heal damage

Conditions:

- When de damaged received is higher than the actual health, health drops to `0` and the character dies.
- When the character is dead, they cannot be healed.
- Characters cannot be healed over `1000` health.
