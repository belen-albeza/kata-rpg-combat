use super::{AttackSource, AttackTarget, DamageDealer, HasHealth, HasLevel};

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

    impl AttackSource for Entity {}
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
