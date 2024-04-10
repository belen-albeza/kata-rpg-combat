pub trait HasHealth {
    fn health(&self) -> u64;

    fn max_health(&self) -> u64;

    fn alive(&self) -> bool {
        self.health() > 0
    }

    fn add_health(&mut self, delta: i64) -> i64;
}

pub trait HasLevel {
    fn level(&self) -> u64;
}

pub trait DamageDealer {
    fn damage(&self) -> u64;
}
