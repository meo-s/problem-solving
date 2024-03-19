// https://www.acmicpc.net/problem/14426

use std::{io::Read, str::FromStr};

#[derive(Debug)]
struct Node {
    next: std::collections::HashMap<u8, Box<Node>>,
}

impl Node {
    fn new() -> Node {
        Node {
            next: std::collections::HashMap::new(),
        }
    }
}

fn scan<'a, T: FromStr>(it: &mut impl Iterator<Item = &'a str>) -> T {
    unsafe { str::parse::<T>(it.next().unwrap()).unwrap_unchecked() }
}

fn main() {
    let mut inp = vec![];
    std::io::stdin()
        .read_to_end(&mut inp)
        .expect("failed to read input");

    let mut it = unsafe { std::str::from_utf8_unchecked(&inp) }
        .trim_end()
        .split_whitespace();

    let n = scan::<usize>(&mut it);
    let m = scan::<usize>(&mut it);
    let mut dict = Node::new();
    for _ in 0..n {
        let mut node = &mut dict;
        for &ch in it.next().unwrap().as_bytes() {
            if !node.next.contains_key(&ch) {
                node.next.insert(ch, Box::<Node>::new(Node::new()));
            }
            node = node.next.get_mut(&ch).unwrap();
        }
    }
    let ans = (0..m)
        .map(|_| it.next().unwrap())
        .filter(|word| {
            let mut node = &dict;
            word.as_bytes().iter().all(|&ch| {
                if let Some(next) = node.next.get(&ch) {
                    node = next;
                    true
                } else {
                    false
                }
            })
        })
        .count();
    println!("{ans}");
}
