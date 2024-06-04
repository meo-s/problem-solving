// https://www.acmicpc.net/problem/2629

use std::{
    collections::HashSet,
    io::{stdin, Read},
};

macro_rules! parse_next {
    ($it:ident, $ty:ty) => {
        $it.next().unwrap().parse::<$ty>().unwrap()
    };
}

macro_rules! scan {
    ($it:ident, $ty:ty) => {
        parse_next!($it, $ty)
    };
    ($it:ident, $arg0:ty, $($args:ty),+ $(,)?) => {
        (parse_next!($it, $arg0), $(parse_next!($it, $args),)+)
    };
}

fn read_input() -> String {
    let mut buf = Default::default();
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let mut indices = Vec::from([0]);
    let mut visited = HashSet::from([0]);
    for _ in 0..scan!(inp, _) {
        let w = scan!(inp, i32);
        for i in 0..indices.len() {
            for idx in [indices[i] - w, indices[i] + w] {
                if !visited.contains(&idx) {
                    visited.insert(idx);
                    indices.push(idx);
                }
            }
        }
    }

    let mut ans = String::new();
    for _ in 0..scan!(inp, _) {
        ans.push(if visited.contains(&scan!(inp, _)) {
            'Y'
        } else {
            'N'
        });
        ans.push(' ');
    }
    println!("{ans}");
}
