#[derive(Debug, PartialEq, Clone, Copy)]
pub struct Character {
    health: u32,
    level: u32,
}

impl Character {
    pub fn new() -> Self {
        return Self {
            health: 1000,
            level: 1,
        };
    }

    pub fn is_dead(&self) -> bool {
        return self.health == 0;
    }

    pub fn attack(&self, damage: u32, other: &mut Character) {
        other.health = other.health.saturating_sub(damage);
    }
}

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
}
