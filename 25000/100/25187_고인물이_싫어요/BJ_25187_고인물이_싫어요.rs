// https://www.acmicpc.net/problem/25187

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
    fn new(n: usize) -> DisjointSet {
        DisjointSet {
            parents: (0..n).collect(),
        }
    }

    fn find(&mut self, u: usize) -> usize {
        if self.parents[u] != u {
            self.parents[u] = self.find(self.parents[u]);
        }
        self.parents[u]
    }

    fn merge(&mut self, u: usize, v: usize) -> bool {
        let up = self.find(u);
        let vp = self.find(v);
        if up != vp {
            self.parents[vp] = up;
        }
        up != vp
    }
}

fn read_input() -> String {
    let mut buf = Default::default();
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let (n, m, q) = scan!(inp, _, _, _);
    let mut water = vec![[0, 0]; n + 1];
    (1..=n).for_each(|it| water[it][scan!(inp, usize)] = 1);

    let mut set = DisjointSet::new(n + 1);
    for _ in 0..m {
        let (u, v) = scan!(inp, _, _);
        let up = set.find(u);
        let vp = set.find(v);
        if set.merge(up, vp) {
            water[up][0] += water[vp][0];
            water[up][1] += water[vp][1];
        }
    }

    let mut ans = String::new();
    for _ in 0..q {
        let up = set.find(scan!(inp, _));
        ans.push(if water[up][0] < water[up][1] {
            '1'
        } else {
            '0'
        });
        ans.push('\n');
    }

    print!("{ans}");
}
