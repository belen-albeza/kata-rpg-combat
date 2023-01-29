use std::{error::Error, fmt};

#[derive(Debug, PartialEq)]
pub struct InvalidTargetError;

impl Error for InvalidTargetError {}

impl fmt::Display for InvalidTargetError {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "Invalid target")
    }
}

#[derive(Debug, PartialEq)]
pub struct FactionError;

impl Error for FactionError {}

impl fmt::Display for FactionError {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "FactionManager error")
    }
}
