use mockall::*;

#[automock]
pub trait HasHealth {
    fn health(&self) -> u64;

    fn max_health(&self) -> u64;

    fn alive(&self) -> bool {
        self.health() > 0
    }

    fn add_health(&mut self, delta: i64) -> i64;
}

#[automock]
pub trait HasLevel {
    fn level(&self) -> u64;
}

#[automock]
pub trait DamageDealer {
    fn damage(&self) -> u64;
}

#[automock]
pub trait HasHealing {
    fn healing(&self) -> u64;
}

#[automock]
pub trait HasID {
    fn id(&self) -> String;
}

#[automock]
pub trait AllianceInformer {
    fn allies(&self, a: &dyn HasID, b: &dyn HasID) -> bool;
}
