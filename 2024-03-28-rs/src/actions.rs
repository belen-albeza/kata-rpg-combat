#[cfg(test)]
pub mod helpers;

pub mod attack;

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

pub trait AttackSource: DamageDealer + HasHealth + HasLevel {}
pub trait AttackTarget: HasHealth + HasLevel {}
