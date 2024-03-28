#[derive(Debug, Clone, Copy, PartialEq)]
pub struct Character {
    health: u64,
    damage: u64,
}

impl Character {
    pub fn new() -> Self {
        Self::default()
    }

    pub fn alive(&self) -> bool {
        self.health > 0
    }
}

impl Default for Character {
    fn default() -> Self {
        Self {
            health: 1000,
            damage: 100,
        }
    }
}

pub struct CharacterBuilder {
    health: u64,
    damage: u64,
}

impl CharacterBuilder {
    pub fn new() -> Self {
        Self {
            health: 1000,
            damage: 1,
        }
    }

    pub fn with_health(&mut self, health: u64) -> &mut Self {
        self.health = health;
        self
    }

    pub fn with_damage(&mut self, damage: u64) -> &mut Self {
        self.damage = damage;
        self
    }

    pub fn build(&self) -> Character {
        let mut c = Character::new();
        c.health = self.health;
        c.damage = self.damage;
        c
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    fn any_character_with_health(health: u64) -> Character {
        CharacterBuilder::new().with_health(health).build()
    }

    #[test]
    pub fn creates_character_with_default_values() {
        let c = Character::default();
        assert_eq!(c.health, 1000);
    }

    #[test]
    pub fn returns_whether_they_are_alive() {
        let dead_chara = any_character_with_health(0);
        assert_eq!(dead_chara.alive(), false);

        let alive_chara = any_character_with_health(1);
        assert_eq!(alive_chara.alive(), true);
    }
}
