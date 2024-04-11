use super::{ActionError, AttackSource, AttackTarget, Result};

pub struct Attack {}

impl Attack {
    pub fn new() -> Self {
        Self {}
    }

    pub fn run(
        &mut self,
        source: &impl AttackSource,
        target: &mut impl AttackTarget,
    ) -> Result<()> {
        if !source.alive() {
            return Err(ActionError::InvalidSource(
                "dead characters cannot attack".to_owned(),
            ));
        }

        let level_diff = source.level() as i64 - target.level() as i64;
        let damage_modifier = match level_diff {
            5.. => 1.5,
            ..=-5 => 0.5,
            _ => 1.0,
        };
        let damage = f64::max(0.0, source.damage() as f64 * damage_modifier) as u64;

        target.add_health(-(damage as i64));

        Ok(())
    }
}

#[cfg(test)]
mod tests {
    use super::super::helpers::EntityBuilder;
    use super::*;
    use mockall::predicate::*;

    #[test]
    fn attack_deals_damage_to_target() {
        let source = EntityBuilder::new().with_damage(100).build();
        let mut target = EntityBuilder::new().with_health(1000).build();
        target
            .expect_add_health()
            .with(eq(-100))
            .times(1)
            .return_const(-100);

        let res = Attack::new().run(&source, &mut target);

        assert!(res.is_ok());
    }

    #[test]
    fn dead_characters_cannot_attack() {
        let source = EntityBuilder::new().with_health(0).build();
        let mut target = EntityBuilder::new().build();

        let res = Attack::new().run(&source, &mut target);

        assert!(res.is_err());
    }

    #[test]
    pub fn attackers_get_a_buff_when_targeting_lower_level_characters() {
        let source = EntityBuilder::new().with_damage(100).with_level(6).build();
        let mut target = EntityBuilder::new().with_health(1000).with_level(1).build();
        target
            .expect_add_health()
            .with(eq(-150))
            .times(1)
            .return_const(-150);

        let res = Attack::new().run(&source, &mut target);

        assert!(res.is_ok());
    }

    #[test]
    pub fn attackers_get_a_debuff_when_targeting_higher_level_characters() {
        let source = EntityBuilder::new().with_damage(100).with_level(1).build();
        let mut target = EntityBuilder::new().with_health(1000).with_level(6).build();
        target
            .expect_add_health()
            .with(eq(-50))
            .times(1)
            .return_const(-50);

        let res = Attack::new().run(&source, &mut target);

        assert!(res.is_ok());
    }
}
