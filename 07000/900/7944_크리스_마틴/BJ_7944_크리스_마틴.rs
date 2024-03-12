// https://www.acmicpc.net/problem/7977

#![allow(unused_must_use)]

use std::io::Read;
use std::str;

fn main() {
    let mut inp = vec![];
    std::io::stdin().read_to_end(&mut inp);

    let mut len = 0;
    let mut counter = std::collections::HashMap::new();
    for ch in [b'A', b'C', b'G', b'T'] {
        counter.insert(ch, 0);
    }
    inp.iter()
        .filter(|ch| ch.is_ascii_alphabetic())
        .for_each(|ch| {
            len += 1;
            *counter.get_mut(ch).unwrap() += 1;
        });

    let mut ans = vec![0u8; len];
    let it = counter.iter().min_by_key(|(_, v)| *v);
    ans.fill(*it.unwrap().0);
    print!("{}\n{}\n", *it.unwrap().1, unsafe {
        str::from_utf8_unchecked(&ans)
    });
}
