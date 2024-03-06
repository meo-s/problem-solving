// https://www.acmicpc.net/problem/10951

use std::io::{Read, Write};

fn main() {
    #![allow(unused_must_use)]
    let mut stdout = std::io::BufWriter::with_capacity(1 << 16, std::io::stdout());
    let mut inp = vec![0_u8; 0];
    std::io::stdin().read_to_end(&mut inp);
    for line in unsafe { std::str::from_utf8_unchecked(&inp) }
        .split('\n')
        .filter(|s| !s.is_empty())
    {
        let res = line
            .trim_end()
            .split(' ')
            .map(|token| str::parse::<u32>(token).unwrap())
            .sum::<u32>();
        writeln!(stdout, "{res}");
    }
}
