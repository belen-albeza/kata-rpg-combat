use crate::factions::HasFactions;

pub type Point = (f64, f64);

pub trait HasHealth {
    fn health(&self) -> u32;
    fn add_health(&mut self, delta: i32);
    fn is_dead(&self) -> bool {
        self.health() <= 0
    }
}

pub trait HasLevel {
    fn level(&self) -> u32;
}

pub trait HasPosition {
    fn position(&self) -> Point;
}

pub trait Target: HasHealth + HasLevel + HasPosition + HasFactions {
    fn id(&self) -> String;
}
