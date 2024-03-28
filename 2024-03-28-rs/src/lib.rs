mod character;

pub fn run() {
    println!("RPG Combat");

    let orc = character::Character::default();
    println!("\tOrc: {:?}", orc);
}
