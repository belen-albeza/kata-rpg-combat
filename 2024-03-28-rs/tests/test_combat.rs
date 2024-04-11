use rpg::actions::attack::Attack;
use rpg::character::CharacterBuilder;
use rpg::traits::HasHealth;

#[test]
fn characters_attack_other_characters() {
    let orc = CharacterBuilder::new().with_damage(100).build();
    let mut elf = CharacterBuilder::new().with_health(1000).build();

    let attack = Attack::new().run(&orc, &mut elf);

    assert!(attack.is_ok());
    assert_eq!(elf.health(), 900);
}
