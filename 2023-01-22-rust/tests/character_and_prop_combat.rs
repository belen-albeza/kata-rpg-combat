use rpgcombat::character::Character;
use rpgcombat::combat::HasHealth;
use rpgcombat::errors::InvalidTargetError;
use rpgcombat::props::Prop;

#[test]
fn test_character_can_attack_prop() {
    let hero = Character::new();
    let mut tree = Prop::new(100, (0.0, 0.0));

    let result = hero.attack(30, &mut tree);

    assert!(result.is_ok());
    assert_eq!(tree.health(), 70);
}

#[test]
fn test_character_cannot_heal_prop() {
    let mut hero = Character::new();
    let mut tree = Prop::new(100, (0.0, 0.0));

    let result = hero.heal(30, Some(&mut tree));

    assert_eq!(result, Err(InvalidTargetError));
}
