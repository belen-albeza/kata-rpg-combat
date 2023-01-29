use std::collections::HashSet;

use crate::errors::FactionError;

pub type Faction = String;

pub trait HasFactions {
    fn join_faction(&mut self, faction: &Faction) -> Result<(), FactionError>;
    fn factions(&self) -> Vec<Faction>;
}

#[derive(Debug, PartialEq, Clone)]
pub struct FactionManager {
    factions: HashSet<Faction>,
}

impl FactionManager {
    pub fn new() -> Self {
        FactionManager {
            factions: HashSet::new(),
        }
    }

    pub fn with_factions(factions: &[Faction]) -> Self {
        FactionManager {
            factions: HashSet::from_iter(factions.iter().map(|x| x.clone())),
        }
    }

    pub fn join(&mut self, faction: &Faction) -> Result<(), FactionError> {
        self.factions.insert(faction.to_owned());
        Ok(())
    }

    pub fn all(&self) -> Vec<Faction> {
        self.factions.iter().map(|x| x.clone()).collect()
    }

    pub fn common_factions_with(&self, others: &[Faction]) -> Vec<Faction> {
        let other_factions: HashSet<Faction> = HashSet::from_iter(others.iter().map(|x| x.clone()));
        let common = self.factions.intersection(&other_factions);

        common.into_iter().map(|x| x.clone()).collect()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_join_faction() {
        let mut fm = FactionManager::new();

        let result = fm.join(&Faction::from("horde"));

        assert!(result.is_ok());
        assert_eq!(fm.factions.len(), 1);
    }

    #[test]
    fn test_returns_factions() {
        let to_join = vec![Faction::from("horde"), Faction::from("croquetas lovers")];
        let fm = FactionManager::with_factions(&to_join);

        let factions = fm.all();

        assert_eq!(factions.len(), 2);
        assert!(factions.contains(&Faction::from("horde")));
        assert!(factions.contains(&Faction::from("croquetas lovers")));
    }

    #[test]
    fn test_returns_the_common_factions_with_another_entity() {
        let fm = FactionManager::with_factions(&vec![
            Faction::from("horde"),
            Faction::from("croquetas lovers"),
        ]);
        let other_factions = vec![
            Faction::from("waka waka"),
            Faction::from("horde"),
            Faction::from("orkz"),
        ];

        let result = fm.common_factions_with(&other_factions);

        assert_eq!(result.len(), 1);
        assert!(result.contains(&Faction::from("horde")));
    }
}
