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
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn test_character_creation() {
        let c = Character::new();
        assert_eq!(c.health, 1000);
        assert_eq!(c.level, 1);
    }

    #[test]
    pub fn test_is_dead() {
        let dead_chara = Character {
            health: 0,
            level: 1,
        };
        let alive_chara = Character {
            health: 1,
            level: 1,
        };

        assert_eq!(dead_chara.is_dead(), true);
        assert_eq!(alive_chara.is_dead(), false);
    }
}
