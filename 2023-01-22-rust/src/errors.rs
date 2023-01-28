use std::{error::Error, fmt};

#[derive(Debug, PartialEq)]
pub struct InvalidTargetError;

impl Error for InvalidTargetError {}

impl fmt::Display for InvalidTargetError {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "Invalid target")
    }
}
