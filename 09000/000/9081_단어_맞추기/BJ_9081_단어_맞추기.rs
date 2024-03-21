fn next_permutation(s: &mut [u8]) {
    let i = match s.windows(2).rposition(|w| w[0] < w[1]) {
        Some(i) => i,
        None => return,
    };
    let j = s[i + 1..].iter().rposition(|&n| s[i] < n).unwrap();
    s.swap(i, i + 1 + j);
    s[i + 1..].reverse();
}

fn main() {
    use std::io::Read;

    let mut inp = vec![];
    std::io::stdin()
        .read_to_end(&mut inp)
        .expect("failed to read input.");

    let mut it = unsafe { std::str::from_utf8_unchecked_mut(&mut inp) }
        .trim_end()
        .split_ascii_whitespace();

    let mut ans = String::new();
    for _ in 0..str::parse::<usize>(it.next().unwrap()).unwrap() {
        let mut s = String::from(it.next().unwrap());
        next_permutation(unsafe { s.as_bytes_mut() });
        ans.push_str(&s);
        ans.push('\n');
    }
    println!("{ans}");
}
