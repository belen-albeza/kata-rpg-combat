use crate::errors::InvalidTargetError;
use std::cmp;

pub trait HasHealth {
    fn add_health(&mut self, delta: i32);
    fn is_dead(&self) -> bool;
}

pub trait HasLevel {
    fn level(&self) -> u32;
}

pub trait Target: HasHealth + HasLevel {}

#[derive(Debug, PartialEq, Clone, Copy)]
pub struct Character {
    health: u32,
    level: u32,
}

impl Character {
    const MAX_HEALTH: u32 = 1000;

    pub fn new() -> Self {
        return Self {
            health: Self::MAX_HEALTH,
            level: 1,
        };
    }

    pub fn attack(&self, damage: u32, other: &mut dyn Target) {
        other.add_health(-(damage as i32));
    }

    pub fn heal(
        &mut self,
        health: u32,
        other: Option<&mut dyn Target>,
    ) -> Result<(), InvalidTargetError> {
        if other.as_ref().map_or(false, |x| !std::ptr::eq(*x, self)) {
            return Err(InvalidTargetError);
        }

        let target = match other {
            Some(x) => x,
            None => self,
        };

        if target.is_dead() {
            return Err(InvalidTargetError);
        }

        target.add_health(health as i32);
        return Ok(());
    }
}

impl HasHealth for Character {
    fn is_dead(&self) -> bool {
        return self.health == 0;
    }

    fn add_health(&mut self, delta: i32) {
        self.health = cmp::min(
            cmp::max(self.health as i32 + delta, 0) as u32,
            Self::MAX_HEALTH,
        );
    }
}

impl HasLevel for Character {
    fn level(&self) -> u32 {
        return self.level;
    }
}

impl Target for Character {}

#[cfg(test)]
mod tests {
    use super::*;

    fn any_character() -> Character {
        Character::new()
    }

    fn any_character_with_health(health: u32) -> Character {
        Character { health, level: 1 }
    }

    #[test]
    pub fn test_character_creation() {
        let c = Character::new();
        assert_eq!(c.health, 1000);
        assert_eq!(c.level, 1);
    }

    #[test]
    pub fn test_is_dead() {
        let dead_chara = any_character_with_health(0);
        let alive_chara = any_character_with_health(1);

        assert_eq!(dead_chara.is_dead(), true);
        assert_eq!(alive_chara.is_dead(), false);
    }

    #[test]
    pub fn test_can_attack_others() {
        let hero = any_character();
        let mut other = any_character_with_health(1000);

        hero.attack(100, &mut other);

        assert_eq!(other.health, 900);
    }

    #[test]
    pub fn test_health_drops_to_zero_when_receiving_damage_larger_than_health() {
        let hero = any_character();
        let mut other = any_character_with_health(1);

        hero.attack(100, &mut other);

        assert_eq!(other.health, 0);
        assert_eq!(other.is_dead(), true);
    }

    #[test]
    pub fn test_can_heal_themselves() {
        let mut hero = any_character_with_health(100);

        let result = hero.heal(50, None);

        assert!(result.is_ok());
        assert_eq!(hero.health, 150);
    }

    #[test]
    pub fn test_cannot_heal_over_max_health() {
        let mut hero = any_character_with_health(999);

        let result = hero.heal(50, None);

        assert!(result.is_ok());
        assert_eq!(hero.health, 1000);
    }

    #[test]
    pub fn test_cannot_heal_dead_characters() {
        let mut hero = any_character();
        let mut other = any_character_with_health(0);
        assert_eq!(other.is_dead(), true);

        let result = hero.heal(50, Some(&mut other));

        assert_eq!(result, Err(InvalidTargetError));
        assert_eq!(other.health, 0);
        assert_eq!(other.is_dead(), true);
    }

    #[test]
    pub fn test_cannot_heal_other_characters() {
        let mut hero = any_character();
        let mut other = any_character_with_health(100);

        let result = hero.heal(50, Some(&mut other));

        assert_eq!(result, Err(InvalidTargetError));
        assert_eq!(other.health, 100);
    }
}
