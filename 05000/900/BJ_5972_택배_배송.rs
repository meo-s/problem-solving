// https://www.acmicpc.net/problem/5972

use std::{
    cmp::Reverse,
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

macro_rules! init {
    ($name:ident) => {
        let mut $name = Default::default();
        stdin().lock().read_to_end(&mut $name).unwrap();
        let $name = unsafe { String::from_utf8_unchecked($name) };
    };
}

fn main() {
    init!(inp);
    let mut inp = inp.split_ascii_whitespace();

    let (n, m) = scan!(inp, _, _);
    let mut g = vec![vec![]; n];
    for _ in 0..m {
        let (u, v, w) = scan!(inp, usize, usize, u32);
        g[u - 1].push((v - 1, w));
        g[v - 1].push((u - 1, w));
    }

    let mut distances = vec![u32::MAX; n];
    distances[0] = 0;

    let mut waypoints = BinaryHeap::from_iter([(Reverse(0), 0)]);
    while let Some((Reverse(cost), u)) = waypoints.pop() {
        if distances[u] == cost {
            for &(v, w) in &g[u] {
                if cost + w < distances[v] {
                    distances[v] = cost + w;
                    waypoints.push((Reverse(distances[v]), v));
                }
            }
        }
    }

    println!("{}", distances[n - 1]);
}
