// https://www.acmicpc.net/problem/11650

use std::io::{Read, Write};

fn main() {
    #![allow(unused_must_use)]

    let mut inp = vec![0_u8; 0];
    std::io::stdin().read_to_end(&mut inp);

    let mut points = Vec::from_iter(
        unsafe { std::str::from_utf8_unchecked(&inp) }
            .split('\n')
            .skip(1)
            .filter(|token| !token.is_empty())
            .map(|ref line| {
                let mut it = line.split_whitespace();
                let x = str::parse::<i32>(it.next().unwrap()).unwrap();
                let y = str::parse::<i32>(it.next().unwrap()).unwrap();
                (x, y)
            }),
    );
    points.sort_by(|lhs, rhs| {
        if lhs.0 == rhs.0 {
            lhs.1.cmp(&rhs.1)
        } else {
            lhs.0.cmp(&rhs.0)
        }
    });

    let mut stdout = std::io::BufWriter::with_capacity(1 << 18, std::io::stdout());
    for pt in points {
        writeln!(stdout, "{} {}", pt.0, pt.1);
    }
}
