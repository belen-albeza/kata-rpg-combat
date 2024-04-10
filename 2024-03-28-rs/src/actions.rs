use std::error::Error;
use std::fmt;

use crate::traits::{DamageDealer, HasHealth, HasLevel};

#[derive(Debug, PartialEq)]
pub enum ActionError {
    InvalidTarget(String),
    InvalidSource(String),
}

impl fmt::Display for ActionError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            Self::InvalidTarget(msg) => write!(f, "Invalid target: {}", msg),
            Self::InvalidSource(msg) => write!(f, "Invalid source: {}", msg),
        }
    }
}

impl Error for ActionError {}

pub type Result<T> = std::result::Result<T, ActionError>;

pub trait Attacker: DamageDealer + HasHealth + HasLevel {}
pub trait AttackTarget: HasHealth + HasLevel {}

pub struct Attack {}

impl Attack {
    pub fn new() -> Self {
        Self {}
    }

    pub fn run(&mut self, source: &impl Attacker, target: &mut impl AttackTarget) -> Result<()> {
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

// #[cfg(test)]
// mod tests {

// }
