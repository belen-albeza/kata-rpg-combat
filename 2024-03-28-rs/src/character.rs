#[derive(Debug, Clone, Copy, PartialEq)]
pub struct Character {
    health: u64,
}

impl Character {
    pub fn new(health: u64) -> Self {
        Self { health }
    }

    pub fn alive(&self) -> bool {
        self.health > 0
    }
}

impl Default for Character {
    fn default() -> Self {
        Self::new(1000)
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn creates_character_with_default_values() {
        let c = Character::default();
        assert_eq!(c.health, 1000);
    }

    #[test]
    pub fn returns_whether_they_are_alive() {
        let dead_chara = Character::new(0);
        assert_eq!(dead_chara.alive(), false);

        let alive_chara = Character::new(1);
        assert_eq!(alive_chara.alive(), true);
    }
}
