use rpg::actions::attack::Attack;
use rpg::character::CharacterBuilder;
use rpg::factions::FactionManager;
use rpg::traits::HasHealth;

#[test]
fn characters_attack_other_characters() {
    let orc = CharacterBuilder::new().with_damage(100).build();
    let mut elf = CharacterBuilder::new().with_health(1000).build();

    let attack = Attack::new().run(&orc, &mut elf, None::<&FactionManager>);

    assert!(attack.is_ok());
    assert_eq!(elf.health(), 900);
}

#[test]
fn characters_cannot_attack_allies() {
    let orc = CharacterBuilder::new().with_damage(100).build();
    let mut troll = CharacterBuilder::new().with_health(1000).build();
    let mut fm = FactionManager::new();
    let _ = fm.add_faction("horde");
    let _ = fm.join_faction("horde", &orc);
    let _ = fm.join_faction("horde", &troll);

    let attack = Attack::new().run(&orc, &mut troll, Some(&fm));

    assert!(attack.is_err());
}

#[test]
fn characters_can_join_factions() {
    let orc = CharacterBuilder::new().with_id("Garrosh").build();

    let mut fm = FactionManager::new();
    _ = fm.add_faction("horde");
    let res = fm.join_faction("horde", &orc);

    assert!(res.is_ok());
    assert_eq!(fm.belongs_to("horde", &orc), Ok(true));
}
