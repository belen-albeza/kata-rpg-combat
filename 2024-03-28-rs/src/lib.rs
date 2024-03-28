use crate::character::CharacterBuilder;

mod character;

pub fn run() {
    println!("RPG Combat");

    let orc = CharacterBuilder::new()
        .with_health(800)
        .with_damage(100)
        .build();
    println!("\tOrc: {:?}", orc);
}
