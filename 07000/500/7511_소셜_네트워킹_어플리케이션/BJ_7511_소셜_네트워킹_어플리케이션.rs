// https://www.acmicpc.net/problem/7511

use std::io::{stdin, Read};

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

    fn merge(&mut self, u: usize, v: usize) {
        let up = self.find(u);
        let vp = self.find(v);
        if up != vp {
            self.parents[vp] = up;
        }
    }
}

fn read_input() -> String {
    let mut buf = Vec::with_capacity(1 << 20);
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let mut ans = String::new();
    for t in 1..=scan!(inp, _) {
        let mut set = DisjointSet::new(scan!(inp, _));
        (0..scan!(inp, _)).for_each(|_| set.merge(scan!(inp, _), scan!(inp, _)));

        if !ans.is_empty() {
            ans.push('\n');
        }
        ans.push_str(&format!("Scenario {t}:\n"));
        for _ in 0..scan!(inp, _) {
            let (u, v) = scan!(inp, _, _);
            if set.find(u) == set.find(v) {
                ans.push_str("1\n");
            } else {
                ans.push_str("0\n");
            }
        }
    }

    print!("{ans}");
}
