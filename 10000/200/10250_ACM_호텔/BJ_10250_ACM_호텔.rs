use std::str::FromStr;

fn scan<'a, T: FromStr>(it: &mut impl Iterator<Item = &'a str>) -> T {
    unsafe { str::parse::<T>(it.next().unwrap_unchecked()).unwrap_unchecked() }
}

fn main() {
    use std::io::Read;

    let mut inp = vec![];
    std::io::stdin()
        .read_to_end(&mut inp)
        .expect("failed to read input.");

    let it = &mut unsafe { std::str::from_utf8_unchecked_mut(&mut inp) }
        .trim_end()
        .split_ascii_whitespace();

    for _ in 0..scan::<usize>(it) {
        let h = scan::<i32>(it);
        scan::<i32>(it);
        let n = scan::<i32>(it);
        println!("{}", ((n - 1) % h + 1) * 100 + (((n - 1) / h) + 1));
    }
}
