// https://www.acmicpc.net/problem/13905

use std::{
    collections::BinaryHeap,
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
    let mut buf = Vec::with_capacity(1 << 18);
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let (n, m, s, e) = scan!(inp, usize, usize, usize, usize);
    let mut g = vec![vec![]; n + 1];
    for _ in 0..m {
        let (u, v, k) = scan!(inp, usize, usize, u32);
        g[u].push((v, k));
        g[v].push((u, k));
    }

    let mut caps = vec![0; n + 1];
    caps[s] = u32::MAX;
    let mut waypoints = BinaryHeap::from_iter([(caps[s], s)]);
    while let Some((k0, u)) = waypoints.pop() {
        for &(v, k) in &g[u] {
            if caps[v] < std::cmp::min(k, k0) {
                caps[v] = std::cmp::min(k, k0);
                waypoints.push((caps[v], v))
            }
        }
    }
    println!("{}", caps[e]);
}
