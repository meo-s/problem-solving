// https://www.acmicpc.net/problem/25381

use std::{collections::VecDeque, io::Read};

fn main() {
    let mut inp = Vec::new();
    std::io::stdin().read_to_end(&mut inp).unwrap();

    let mut matched = vec![false; inp.len()];
    let mut pending = VecDeque::new();
    for (i, ch) in inp.iter().enumerate() {
        if *ch == b'C' && !pending.is_empty() {
            matched[pending.pop_front().unwrap()] = true;
        } else if *ch == b'B' {
            pending.push_back(i);
        }
    }
    pending.clear();
    for (i, ch) in inp.iter().enumerate() {
        if *ch == b'B' && !pending.is_empty() && !matched[i] {
            matched[pending.pop_front().unwrap()] = true;
        } else if *ch == b'A' {
            pending.push_back(i);
        }
    }
    println!("{}", matched.into_iter().filter(|v| *v).count());
}
