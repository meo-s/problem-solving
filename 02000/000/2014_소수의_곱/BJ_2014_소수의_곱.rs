// https://www.acmicpc.net/problem/2014

use std::{
    cmp::Reverse,
    collections::{BinaryHeap, HashSet},
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
    let mut buf = Vec::with_capacity(1 << 20);
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let (n, m) = scan!(inp, _, usize);
    let factors: Vec<_> = (0..n).map(|_| scan!(inp, u64)).collect();
    let mut idx = 0;
    let mut bottom = 0;
    let mut visited = HashSet::new();
    let mut candidates = BinaryHeap::from([Reverse(1)]);
    while idx < m {
        let Reverse(base) = candidates.pop().unwrap();
        for factor in &factors {
            if idx + candidates.len() < m || factor * base < bottom {
                if visited.insert(factor * base) {
                    candidates.push(Reverse(factor * base));
                    bottom = bottom.max(factor * base);
                }
            }
        }
        idx += 1;
    }
    println!("{}", candidates.peek().unwrap().0);
}
