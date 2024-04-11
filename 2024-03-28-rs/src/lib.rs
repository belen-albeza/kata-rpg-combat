pub mod actions;
pub mod character;
pub mod traits;

use crate::character::CharacterBuilder;

impl actions::Attacker for character::Character {}
impl actions::AttackTarget for character::Character {}
impl actions::Healer for character::Character {}
impl actions::HealTarget for character::Character {}

pub fn run() {
    println!("RPG Combat");

    let orc = CharacterBuilder::new()
        .with_health(800)
        .with_damage(100)
        .with_level(6)
        .build();
    let mut elf = CharacterBuilder::new()
        .with_health(1000)
        .with_damage(50)
        .with_healing(80)
        .build();
    println!("\tGarrosh: {:?}", orc);
    println!("\tMalfurion: {:?}", elf);

    _ = actions::Attack::new().run(&orc, &mut elf);
    println!("");
    println!("> Garrosh attacks Malfurion");
    println!("\tGarrosh: {:?}", orc);
    println!("\tMalfurion: {:?}", elf);

    _ = actions::HealSelf::new().run(&mut elf);
    println!("");
    println!("> Malfurion heals himself");
    println!("\tGarrosh: {:?}", orc);
    println!("\tMalfurion: {:?}", elf);
}
