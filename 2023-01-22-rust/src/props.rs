use crate::combat::{HasHealth, HasLevel, HasPosition, Point, Target};
use crate::errors::FactionError;
use crate::factions::{Faction, HasFactions};
use std::cmp;
use uuid::Uuid;

#[derive(Debug, PartialEq, Clone)]
pub struct Prop {
    id: Uuid,
    health: u32,
    initial_health: u32,
    position: Point,
}

impl Prop {
    pub fn new(health: u32, position: Point) -> Self {
        Prop {
            health,
            id: Uuid::new_v4(),
            initial_health: health,
            position,
        }
    }
}

impl HasHealth for Prop {
    fn add_health(&mut self, delta: i32) {
        self.health = cmp::min(
            self.initial_health,
            cmp::max(0, (self.health as i32) + delta) as u32,
        );
    }

    fn health(&self) -> u32 {
        self.health
    }
}

impl HasLevel for Prop {
    fn level(&self) -> u32 {
        1
    }
}

impl HasPosition for Prop {
    fn position(&self) -> Point {
        self.position
    }
}

impl HasFactions for Prop {
    fn factions(&self) -> Vec<Faction> {
        vec![]
    }

    fn join_faction(&mut self, _: &Faction) -> Result<(), FactionError> {
        return Err(FactionError);
    }
}

impl Target for Prop {
    fn id(&self) -> String {
        self.id.to_string()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    fn any_prop() -> Prop {
        Prop::new(100, (0.0, 0.0))
    }

    fn any_prop_with_health(health: u32) -> Prop {
        Prop::new(health, (0.0, 0.0))
    }

    #[test]
    pub fn test_creation() {
        let p = Prop::new(100, (5.0, 2.0));

        assert_eq!(p.health, 100);
        assert_eq!(p.position, (5.0, 2.0));
    }

    #[test]
    pub fn test_has_level_1() {
        let p = any_prop();

        assert_eq!(p.level(), 1);
    }

    #[test]
    pub fn test_returns_its_health() {
        let p = any_prop_with_health(50);

        assert_eq!(p.health(), 50);
    }

    #[test]
    pub fn test_add_health() {
        let mut p = any_prop_with_health(50);

        p.add_health(-30);

        assert_eq!(p.health, 20);
    }

    #[test]
    pub fn test_health_drops_to_zero_on_overflow() {
        let mut p = any_prop_with_health(50);

        p.add_health(-100);

        assert_eq!(p.health, 0);
    }

    #[test]
    pub fn test_health_cannot_go_over_initial_value() {
        let mut p = any_prop_with_health(50);

        p.add_health(-1);
        p.add_health(2);

        assert_eq!(p.health, 50);
    }

    #[test]
    pub fn test_returns_its_position() {
        let p = Prop::new(100, (1.0, 2.0));

        assert_eq!(p.position(), (1.0, 2.0));
    }

    #[test]
    pub fn test_return_empty_list_of_factions() {
        let p = any_prop();

        assert_eq!(p.factions(), Vec::<Faction>::new());
    }

    #[test]
    pub fn test_join_faction_returns_error() {
        let mut p = any_prop();

        let result = p.join_faction(&Faction::from("horde"));

        assert_eq!(result, Err(FactionError));
    }
}
