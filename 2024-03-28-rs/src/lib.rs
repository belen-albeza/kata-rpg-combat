use crate::character::CharacterBuilder;

mod character;

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

    _ = orc.attack(&mut elf);
    println!("");
    println!("> Garrosh attacks Malfurion");
    println!("\tGarrosh: {:?}", orc);
    println!("\tMalfurion: {:?}", elf);

    _ = elf.heal();
    println!("");
    println!("> Malfurion heals himself");
    println!("\tGarrosh: {:?}", orc);
    println!("\tMalfurion: {:?}", elf);
}
