use std::error::Error;
use std::fmt;

use crate::traits::{DamageDealer, HasHealth, HasLevel};

#[derive(Debug, PartialEq)]
pub enum ActionError {
    InvalidTarget(String),
    InvalidSource(String),
}

impl fmt::Display for ActionError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            Self::InvalidTarget(msg) => write!(f, "Invalid target: {}", msg),
            Self::InvalidSource(msg) => write!(f, "Invalid source: {}", msg),
        }
    }
}

impl Error for ActionError {}

pub type Result<T> = std::result::Result<T, ActionError>;

pub trait Attacker: DamageDealer + HasHealth + HasLevel {}
pub trait AttackTarget: HasHealth + HasLevel {}

pub struct Attack {}

impl Attack {
    pub fn new() -> Self {
        Self {}
    }

    pub fn run(&mut self, source: &impl Attacker, target: &mut impl AttackTarget) -> Result<()> {
        if !source.alive() {
            return Err(ActionError::InvalidSource(
                "dead characters cannot attack".to_owned(),
            ));
        }

        let level_diff = source.level() as i64 - target.level() as i64;
        let damage_modifier = match level_diff {
            5.. => 1.5,
            ..=-5 => 0.5,
            _ => 1.0,
        };
        let damage = f64::max(0.0, source.damage() as f64 * damage_modifier) as u64;

        target.add_health(-(damage as i64));

        Ok(())
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use mockall::predicate::*;
    use test_helpers::EntityBuilder;

    #[test]
    fn attack_deals_damage_to_target() {
        let source = EntityBuilder::new().with_damage(100).build();
        let mut target = EntityBuilder::new().with_health(1000).build();
        target
            .expect_add_health()
            .with(eq(-100))
            .times(1)
            .return_const(-100);

        let res = Attack::new().run(&source, &mut target);

        assert!(res.is_ok());
    }

    #[test]
    fn dead_characters_cannot_attack() {
        let source = EntityBuilder::new().with_health(0).build();
        let mut target = EntityBuilder::new().build();

        let res = Attack::new().run(&source, &mut target);

        assert!(res.is_err());
    }

    #[test]
    pub fn attackers_get_a_buff_when_targeting_lower_level_characters() {
        let source = EntityBuilder::new().with_damage(100).with_level(6).build();
        let mut target = EntityBuilder::new().with_health(1000).with_level(1).build();
        target
            .expect_add_health()
            .with(eq(-150))
            .times(1)
            .return_const(-150);

        let res = Attack::new().run(&source, &mut target);

        assert!(res.is_ok());
    }

    #[test]
    pub fn attackers_get_a_debuff_when_targeting_higher_level_characters() {
        let source = EntityBuilder::new().with_damage(100).with_level(1).build();
        let mut target = EntityBuilder::new().with_health(1000).with_level(6).build();
        target
            .expect_add_health()
            .with(eq(-50))
            .times(1)
            .return_const(-50);

        let res = Attack::new().run(&source, &mut target);

        assert!(res.is_ok());
    }
}

#[cfg(test)]
mod test_helpers {
    use super::*;
    use mockall::predicate::*;
    use mockall::*;

    mock! {
        #[derive(Debug)]
        pub Entity {}
        impl DamageDealer for Entity {
            fn damage(&self) -> u64;
        }
        impl HasHealth for Entity {
            fn health(&self) -> u64;
            fn alive(&self) -> bool;
            fn max_health(&self) -> u64;
            fn add_health(&mut self, delta: i64) -> i64;
        }
        impl HasLevel for Entity {
            fn level(&self) -> u64;
        }

        impl Attacker for Entity {}
        impl AttackTarget for Entity {}
    }

    pub struct EntityBuilder {
        level: u64,
        health: u64,
        damage: u64,
    }

    impl EntityBuilder {
        pub fn new() -> Self {
            Self {
                level: 1,
                health: 1000,
                damage: 1,
            }
        }

        pub fn build(&self) -> MockEntity {
            let mut mock = MockEntity::new();
            mock.expect_level().return_const(self.level);
            mock.expect_health().return_const(self.health);
            mock.expect_alive().return_const(self.health > 0);
            mock.expect_damage().return_const(self.damage);

            mock
        }

        pub fn with_level(&mut self, level: u64) -> &mut Self {
            self.level = level;
            self
        }

        pub fn with_health(&mut self, health: u64) -> &mut Self {
            self.health = health;
            self
        }

        pub fn with_damage(&mut self, damage: u64) -> &mut Self {
            self.damage = damage;
            self
        }
    }
}
