// https://www.acmicpc.net/problem/16562

use std::{
    collections::HashMap,
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

struct DisjointSet {
    parents: Vec<usize>,
}

impl DisjointSet {
    fn new(n: usize) -> Self {
        Self {
            parents: (0..n).collect(),
        }
    }

    fn find(&mut self, u: usize) -> usize {
        if self.parents[u] != u {
            self.parents[u] = self.find(self.parents[u]);
        }
        self.parents[u]
    }

    fn merge(&mut self, (u, v): (usize, usize)) {
        let up = self.find(u);
        let vp = self.find(v);
        if up != vp {
            self.parents[vp] = up;
        }
    }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let (n, m, k) = scan!(inp, _, _, _);
    let costs: Vec<_> = (0..n).map(|_| scan!(inp, u64)).collect();
    let mut set = DisjointSet::new(n + 1);
    (0..m).for_each(|_| set.merge(scan!(inp, _, _)));

    let mut min_costs = HashMap::<usize, _>::new();
    for u in 1..=n {
        let up = set.find(u);
        min_costs.insert(
            up,
            if let Some(&c) = min_costs.get(&up) {
                costs[u - 1].min(c)
            } else {
                costs[u - 1]
            },
        );
    }

    let ans = min_costs.values().sum::<u64>();
    if ans <= k {
        println!("{ans}");
    } else {
        print!("Oh no");
    }
}
