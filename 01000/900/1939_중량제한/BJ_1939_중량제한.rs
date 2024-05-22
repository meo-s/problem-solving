// https://www.acmicpc.net/problem/1939

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
    let mut buf = Vec::with_capacity(1 << 20);
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let (n, m) = scan!(inp, usize, _);
    let mut g = vec![vec![]; n + 1];
    for _ in 0..m {
        let (u, v, w) = scan!(inp, usize, usize, u32);
        g[u].push((v, w));
        g[v].push((u, w));
    }

    let (s, e) = scan!(inp, usize, usize);
    let mut waypoints = BinaryHeap::from([(u32::MAX, s)]);
    let mut weights = vec![0; n + 1];
    weights[s] = u32::MAX;
    while let Some((wu, u)) = waypoints.pop() {
        if weights[u] == wu {
            for &(v, wv) in &g[u] {
                let nw = std::cmp::min(wu, wv);
                if weights[v] < nw {
                    waypoints.push((nw, v));
                    weights[v] = nw;
                }
            }
        }
    }

    println!("{}", weights[e]);
}
