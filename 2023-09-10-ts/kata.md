# RPG Combat

> NOTE:This is an slighltly adapted version of the kata found at
> https://sammancoaching.org/kata_descriptions/rpg_combat.html
> (License: CC-BY-SA-4.0)

This Kata that has you building simple combat rules as for a role-playing game (RPG). The domain doesn’t include a map or any other character skills apart from their ability to damage and heal one another.

The problem is broken down into several user stories to help you to focus on doing one thing at a time. Complete one user story before starting on the next one. Be sure to work on the problem in small steps and pay close attention to the design of both the code and the automated tests in every step.

## Iterations

### Damage and Health

- All Characters, when created, have:
  - Health, starting at `1000`
  - May be Alive or Dead, starting Alive
- Characters can Deal Damage to Characters.
  - Damage is subtracted from Health
  - When damage received exceeds current Health, Health becomes `0` and the character dies
  - A Character cannot Deal Damage to itself
- A Character can Heal themselves.
  - Dead characters cannot be healed
  - Healing cannot raise health above `1000`.

### Levels

- All characters have a Level, starting at `1`
  - A Character cannot have a health above `1000` until they reach level 6, when the maximum increases to `1500`
- When dealing damage:
  - If the target is `5` or more Levels above the attacker, Damage is reduced by `50%`
  - If the target is `5` or more Levels below the attacker, Damage is increased by `50%`
