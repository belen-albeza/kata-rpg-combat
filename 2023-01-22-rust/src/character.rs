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
}
