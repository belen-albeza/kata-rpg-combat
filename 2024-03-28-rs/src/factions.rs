use std::collections::{HashMap, HashSet};
use std::fmt;

use std::error::Error;

use crate::traits::HasID;

#[derive(Debug, PartialEq)]
pub enum FactionError {
    FactionDoesNotExist(String),
    DuplicateFaction(String),
    MemberDoesNotExist(String, String),
    DuplicateMember(String, String),
}

impl fmt::Display for FactionError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            Self::FactionDoesNotExist(id) => write!(f, "Faction does not exist: `{}`", id),
            Self::DuplicateFaction(id) => write!(f, "Faction `{}` already exists", id),
            Self::MemberDoesNotExist(faction_id, member_id) => write!(
                f,
                "Member with ID `{}` does not belong to faction `{}`",
                member_id, faction_id
            ),
            Self::DuplicateMember(faction_id, member_id) => write!(
                f,
                "Faction `{}` already has member with ID `{}`",
                faction_id, member_id
            ),
        }
    }
}

impl Error for FactionError {}

pub type Result<T> = std::result::Result<T, FactionError>;

pub struct FactionManager {
    factions: HashMap<String, Faction>,
}

impl FactionManager {
    pub fn new() -> Self {
        Self {
            factions: HashMap::new(),
        }
    }

    pub fn add_faction(&mut self, id: &str) -> Result<()> {
        if self.factions.contains_key(id) {
            return Err(FactionError::DuplicateFaction(id.to_owned()));
        }

        self.factions.insert(id.to_owned(), Faction::new());
        Ok(())
    }

    pub fn join_faction(&mut self, id: &str, member: &impl HasID) -> Result<()> {
        let f = self
            .factions
            .get_mut(id)
            .ok_or(FactionError::FactionDoesNotExist(id.to_owned()))?;

        match f.insert(member.id()) {
            false => Err(FactionError::DuplicateMember(id.to_owned(), member.id())),
            true => Ok(()),
        }
    }

    pub fn leave_faction(&mut self, id: &str, member: &impl HasID) -> Result<()> {
        match self
            .factions
            .get_mut(id)
            .ok_or(FactionError::FactionDoesNotExist(id.to_owned()))?
            .remove(&member.id())
        {
            true => Ok(()),
            false => Err(FactionError::MemberDoesNotExist(id.to_owned(), member.id())),
        }
    }

    pub fn belongs_to(&self, id: &str, member: &impl HasID) -> Result<bool> {
        Ok(self
            .factions
            .get(id)
            .ok_or(FactionError::FactionDoesNotExist(id.to_owned()))?
            .contains(&member.id()))
    }
}

type Faction = HashSet<String>;

#[cfg(test)]
mod tests {
    use super::*;

    use mockall::predicate::*;
    use mockall::*;

    mock! {
        #[derive(Debug)]
        pub Member {}
        impl HasID for Member {
            fn id(&self) -> String;
        }
    }

    #[test]
    fn manager_is_created_with_no_factions() {
        let fm = FactionManager::new();
        assert_eq!(fm.factions.len(), 0);
    }

    #[test]
    fn adds_a_new_faction_with_no_members() {
        let mut fm = FactionManager::new();
        let res = fm.add_faction("horde");

        assert!(res.is_ok());
    }

    #[test]
    fn returns_error_for_duplicate_factions() {
        let mut fm = FactionManager::new();
        _ = fm.add_faction("horde");

        let res = fm.add_faction("horde");

        assert!(res.is_err());
    }

    #[test]
    fn adds_members_to_faction() {
        let mut fm = FactionManager::new();
        _ = fm.add_faction("horde");
        let mut member = MockMember::new();
        member.expect_id().return_const("garrosh");

        let res = fm.join_faction("horde", &member);

        assert!(res.is_ok());
        assert_eq!(fm.belongs_to("horde", &member), Ok(true));
    }

    #[test]
    fn member_leaves_faction() {
        let mut member = MockMember::new();
        member.expect_id().return_const("garrosh");
        let mut fm = FactionManager::new();
        _ = fm.add_faction("horde");
        _ = fm.join_faction("horde", &member);

        let res = fm.leave_faction("horde", &member);

        assert!(res.is_ok());
        assert_eq!(fm.belongs_to("horde", &member), Ok(false));
    }

    #[test]
    fn factions_are_created_with_no_members() {
        let f = Faction::new();
        assert_eq!(f.len(), 0);
    }
}
