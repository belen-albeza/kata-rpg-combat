use crate::errors::InvalidTargetError;
use std::cmp;
use uuid::Uuid;

pub trait HasHealth {
    fn add_health(&mut self, delta: i32);
    fn is_dead(&self) -> bool;
}

pub trait HasLevel {
    fn level(&self) -> u32;
}

pub trait Target: HasHealth + HasLevel {
    fn id(&self) -> String;
}

#[derive(Debug, PartialEq, Clone, Copy)]
pub struct Character {
    health: u32,
    level: u32,
    id: Uuid,
}

impl Character {
    const MAX_HEALTH: u32 = 1000;

    pub fn new() -> Self {
        return Self {
            id: Uuid::new_v4(),
            health: Self::MAX_HEALTH,
            level: 1,
        };
    }

    pub fn with_health_and_level(health: u32, level: u32) -> Self {
        let mut chara = Self::new();
        chara.level = level;
        chara.health = std::cmp::min(health, Self::MAX_HEALTH);

        chara
    }

    pub fn attack(&self, damage: u32, other: &mut dyn Target) -> Result<u32, InvalidTargetError> {
        if self.id() == other.id() {
            return Err(InvalidTargetError);
        }

        let dealt_damage =
            (self.damage_modifier_for_target(other) * (damage as f64)).floor() as u32;

        other.add_health(-(dealt_damage as i32));
        Ok(dealt_damage)
    }

    pub fn heal(
        &mut self,
        health: u32,
        other: Option<&mut dyn Target>,
    ) -> Result<(), InvalidTargetError> {
        if other.as_ref().map_or(false, |x| x.id() != self.id()) {
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

    fn damage_modifier_for_target(&self, other: &dyn Target) -> f64 {
        let diff = self.level() as i32 - other.level() as i32;

        match diff {
            ..=-5 => 0.5,
            5.. => 1.5,
            _ => 1.0,
        }
    }
}

impl HasHealth for Character {
    fn is_dead(&self) -> bool {
        self.health == 0
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
        self.level
    }
}

impl Target for Character {
    fn id(&self) -> String {
        self.id.to_string()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    fn any_character() -> Character {
        Character::new()
    }

    fn any_character_with_health(health: u32) -> Character {
        Character::with_health_and_level(health, 1)
    }

    fn any_character_with_health_and_level(health: u32, level: u32) -> Character {
        Character::with_health_and_level(health, level)
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

        let result = hero.attack(100, &mut other);

        assert_eq!(result, Ok(100));
        assert_eq!(other.health, 900);
    }

    #[test]
    pub fn test_cannot_attack_themselves() {
        let hero = any_character();

        let result = hero.attack(100, &mut hero.clone());

        assert_eq!(result, Err(InvalidTargetError));
    }

    #[test]
    pub fn test_health_drops_to_zero_when_receiving_damage_larger_than_health() {
        let hero = any_character();
        let mut other = any_character_with_health(1);

        let result = hero.attack(100, &mut other);

        assert_eq!(result, Ok(100));
        assert_eq!(other.health, 0);
        assert_eq!(other.is_dead(), true);
    }

    #[test]
    pub fn test_attack_gets_boosted_when_target_is_5_or_more_levels_lower() {
        let hero = any_character_with_health_and_level(1000, 6);
        let mut other = any_character_with_health_and_level(1000, 1);

        let result = hero.attack(100, &mut other);

        assert_eq!(result, Ok(150));
        assert_eq!(other.health, 850);
    }

    #[test]
    pub fn test_attack_gets_reduced_when_target_is_5_or_more_levels_higher() {
        let hero = any_character_with_health_and_level(1000, 1);
        let mut other = any_character_with_health_and_level(1000, 6);

        let result = hero.attack(100, &mut other);

        assert_eq!(result, Ok(50));
        assert_eq!(other.health, 950);
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
