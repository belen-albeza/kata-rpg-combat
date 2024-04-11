use crate::traits::{DamageDealer, HasHealth, HasLevel};
use std::cmp::{max, min};

const MAX_HEALTH_LO_LEVEL: u64 = 1000;
const MAX_HEALTH_HI_LEVEL: u64 = 1500;

#[derive(Debug, Clone, Copy, PartialEq)]
pub struct Character {
    health: u64,
    damage: u64,
    healing: u64,
    level: u64,
}

impl Character {
    fn new() -> Self {
        Self::default()
    }

    pub fn attack(&self, other: &mut (impl HasHealth + HasLevel)) -> Result<(), String> {
        if !self.alive() {
            return Err("dead characters cannot attack".to_string());
        }

        let level_diff = self.level as i64 - other.level() as i64;
        let damage_modifier = match level_diff {
            5.. => 1.5,
            ..=-5 => 0.5,
            _ => 1.0,
        };
        let damage = f64::max(0.0, self.damage as f64 * damage_modifier) as u64;

        other.add_health(-(damage as i64));

        Ok(())
    }

    pub fn heal(&mut self) -> Result<(), String> {
        if !self.alive() {
            return Err("dead characters cannot heal".to_string());
        }

        self.health = min(self.health + self.healing, self.max_health());
        Ok(())
    }
}

impl Default for Character {
    fn default() -> Self {
        Self {
            level: 1,
            health: 1000,
            damage: 1,
            healing: 1,
        }
    }
}

impl HasLevel for Character {
    fn level(&self) -> u64 {
        self.level
    }
}

impl HasHealth for Character {
    fn health(&self) -> u64 {
        self.health
    }

    fn add_health(&mut self, delta: i64) -> i64 {
        let old_health = self.health() as i64;
        let new_health = min(
            max(0, self.health() as i64 + delta),
            self.max_health() as i64,
        );

        self.health = new_health as u64;

        new_health - old_health
    }

    fn max_health(&self) -> u64 {
        if self.level >= 6 {
            MAX_HEALTH_HI_LEVEL
        } else {
            MAX_HEALTH_LO_LEVEL
        }
    }
}

impl DamageDealer for Character {
    fn damage(&self) -> u64 {
        self.damage
    }
}

pub struct CharacterBuilder {
    health: Option<u64>,
    damage: u64,
    healing: u64,
    level: u64,
}

impl CharacterBuilder {
    pub fn new() -> Self {
        Self {
            health: None,
            damage: 1,
            healing: 1,
            level: 1,
        }
    }

    pub fn with_health(&mut self, health: u64) -> &mut Self {
        self.health = Some(health);
        self
    }

    pub fn with_damage(&mut self, damage: u64) -> &mut Self {
        self.damage = damage;
        self
    }

    pub fn with_healing(&mut self, healing: u64) -> &mut Self {
        self.healing = healing;
        self
    }

    pub fn with_level(&mut self, level: u64) -> &mut Self {
        self.level = max(1, level);
        self
    }

    pub fn build(&self) -> Character {
        let mut c = Character::new();
        c.health = match self.health {
            None => self.default_health(),
            Some(health) => health,
        };
        c.damage = self.damage;
        c.healing = self.healing;
        c.level = self.level;
        c
    }

    fn default_health(&self) -> u64 {
        if self.level >= 6 {
            MAX_HEALTH_HI_LEVEL
        } else {
            MAX_HEALTH_LO_LEVEL
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    fn any_target(health: u64) -> Character {
        CharacterBuilder::new().with_health(health).build()
    }

    fn any_attacker(damage: u64) -> Character {
        CharacterBuilder::new().with_damage(damage).build()
    }

    #[test]
    pub fn creates_character_with_default_values() {
        let c = Character::default();
        assert_eq!(c.health, 1000);
        assert_eq!(c.level, 1);
    }

    #[test]
    pub fn creates_character_with_builder() {
        let c = CharacterBuilder::new()
            .with_health(1)
            .with_damage(2)
            .with_healing(3)
            .with_level(4)
            .build();
        assert_eq!(c.health, 1);
        assert_eq!(c.damage, 2);
        assert_eq!(c.healing, 3);
        assert_eq!(c.level, 4);
    }

    #[test]
    pub fn high_level_characters_get_1500_health() {
        let c = CharacterBuilder::new().with_level(6).build();

        assert_eq!(c.health, 1500);
    }

    #[test]
    pub fn minimum_level_is_1() {
        let c = CharacterBuilder::new().with_level(0).build();
        assert_eq!(c.level, 1);
    }

    #[test]
    pub fn returns_whether_they_are_alive() {
        let dead_chara = any_target(0);
        assert_eq!(dead_chara.alive(), false);

        let alive_chara = any_target(1);
        assert_eq!(alive_chara.alive(), true);
    }

    #[test]
    pub fn health_does_not_get_below_zero() {
        let attacker = any_attacker(100);
        let mut target = any_target(1);

        let res = attacker.attack(&mut target);

        assert!(res.is_ok());
        assert_eq!(target.health, 0);
    }

    #[test]
    pub fn characters_heal_hp() {
        let mut healer = CharacterBuilder::new()
            .with_health(900)
            .with_healing(50)
            .build();

        let res = healer.heal();

        assert!(res.is_ok());
        assert_eq!(healer.health, 950);
    }

    #[test]
    pub fn dead_characters_cannot_heal() {
        let mut healer = CharacterBuilder::new()
            .with_health(0)
            .with_healing(50)
            .build();

        let res = healer.heal();

        assert!(res.is_err());
    }

    #[test]
    pub fn health_does_not_go_above_1000() {
        let mut healer = CharacterBuilder::new()
            .with_health(900)
            .with_healing(200)
            .build();

        let res = healer.heal();

        assert!(res.is_ok());
        assert_eq!(healer.health, 1000);
    }

    #[test]
    pub fn health_cap_raises_to_1500_for_level_6_and_beyond() {
        let mut healer = CharacterBuilder::new()
            .with_health(1400)
            .with_healing(200)
            .with_level(6)
            .build();

        let res = healer.heal();

        assert!(res.is_ok());
        assert_eq!(healer.health, 1500);
    }
}
