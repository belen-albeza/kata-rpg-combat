use crate::character::CharacterBuilder;

mod character;

pub fn run() {
    println!("RPG Combat");

    let orc = CharacterBuilder::new()
        .with_health(800)
        .with_damage(100)
        .build();
    let mut elf = CharacterBuilder::new()
        .with_health(1000)
        .with_damage(50)
        .build();
    println!("\tGarrosh: {:?}", orc);
    println!("\tMalfurion: {:?}", elf);
    println!("");

    _ = orc.attack(&mut elf);
    println!("> Garrosh attacks Malfurion");
    println!("\tGarrosh: {:?}", orc);
    println!("\tMalfurion: {:?}", elf);
}
