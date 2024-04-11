use super::{ActionError, Healer, Result};

pub struct HealSelf {}

impl HealSelf {
    pub fn new() -> Self {
        Self {}
    }

    pub fn run(&self, source: &mut impl Healer) -> Result<()> {
        if !source.alive() {
            return Err(ActionError::InvalidSource(
                "dead characters cannot heal".to_owned(),
            ));
        }

        source.add_health(source.healing() as i64);

        Ok(())
    }
}

#[cfg(test)]
mod tests {
    use super::super::helpers::EntityBuilder;
    use super::*;
    use mockall::predicate::*;

    #[test]
    fn heal_recovers_hp() {
        let mut healer = EntityBuilder::new()
            .with_health(900)
            .with_healing(100)
            .build();
        healer
            .expect_add_health()
            .with(eq(100))
            .times(1)
            .return_const(100);

        let res = HealSelf::new().run(&mut healer);

        assert!(res.is_ok());
    }

    #[test]
    pub fn dead_healers_cannot_heal() {
        let mut healer = EntityBuilder::new().with_health(0).build();

        let res = HealSelf::new().run(&mut healer);

        assert!(res.is_err());
    }
}
