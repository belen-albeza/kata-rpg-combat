use rpg::actions::attack::Attack;
use rpg::character::CharacterBuilder;
use rpg::traits::HasHealth;

#[test]
fn characters_attack_other_characters() {
    let orc = CharacterBuilder::new().with_damage(100).build();
    let mut elf = CharacterBuilder::new().with_health(1000).build();
    let mut attack = Attack::new();

    let res = attack.run(&orc, &mut elf);

    assert!(res.is_ok());
    assert_eq!(elf.health(), 900);
}
