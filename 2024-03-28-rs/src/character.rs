use std::cmp::{max, min};

const MAX_HEALTH: u64 = 1000;

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

    pub fn alive(&self) -> bool {
        self.health > 0
    }

    pub fn attack(&self, other: &mut Character) -> Result<(), String> {
        if !self.alive() {
            return Err("dead characters cannot attack".to_string());
        }

        let new_health = other.health.saturating_sub(self.damage);
        other.health = new_health;

        Ok(())
    }

    pub fn heal(&mut self) -> Result<(), String> {
        if !self.alive() {
            return Err("dead characters cannot heal".to_string());
        }

        self.health = min(self.health + self.healing, MAX_HEALTH);
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

pub struct CharacterBuilder {
    health: u64,
    damage: u64,
    healing: u64,
    level: u64,
}

impl CharacterBuilder {
    pub fn new() -> Self {
        Self {
            health: 1000,
            damage: 1,
            healing: 1,
            level: 1,
        }
    }

    pub fn with_health(&mut self, health: u64) -> &mut Self {
        self.health = health;
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
        c.health = self.health;
        c.damage = self.damage;
        c.healing = self.healing;
        c.level = self.level;
        c
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
    pub fn returns_whether_they_are_alive() {
        let dead_chara = any_target(0);
        assert_eq!(dead_chara.alive(), false);

        let alive_chara = any_target(1);
        assert_eq!(alive_chara.alive(), true);
    }

    #[test]
    pub fn characters_deal_damage() {
        let attacker = any_attacker(100);
        let mut target = any_target(1000);

        let res = attacker.attack(&mut target);

        assert!(res.is_ok());
        assert_eq!(target.health, 900);
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
    pub fn dead_characters_cannot_attack() {
        let attacker = CharacterBuilder::new()
            .with_health(0)
            .with_damage(100)
            .build();
        let mut target = any_target(1000);

        let res = attacker.attack(&mut target);

        assert!(res.is_err());
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
    pub fn health_does_not_go_above_zero() {
        let mut healer = CharacterBuilder::new()
            .with_health(900)
            .with_healing(200)
            .build();
        let res = healer.heal();

        assert!(res.is_ok());
        assert_eq!(healer.health, 1000);
    }
}
