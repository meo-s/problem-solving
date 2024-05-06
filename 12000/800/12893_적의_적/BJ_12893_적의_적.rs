// https://www.acmicpc.net/problem/12893

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

type Graph = Vec<Vec<usize>>;

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
enum Relation {
    None = 0,
    Amicable = 1,
    Hostile = 2,
}

fn read_input() -> String {
    let mut buf = Default::default();
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn read_graph<'a>(inp: &mut impl Iterator<Item = &'a str>) -> Graph {
    let mut g = vec![vec![]; scan!(inp, usize)];
    for _ in 0..scan!(inp, usize) {
        let (u, v) = scan!(inp, usize, usize);
        g[u - 1].push(v - 1);
        g[v - 1].push(u - 1);
    }
    g
}

fn validate(g: &Graph, r: &mut Vec<Relation>, u: usize, p: usize) -> bool {
    if u == p {
        r[u] = Relation::Amicable;
    } else {
        r[u] = if r[p] == Relation::Amicable {
            Relation::Hostile
        } else {
            Relation::Amicable
        };
    }
    for &v in g[u].iter() {
        if (r[v] == Relation::None && !validate(g, r, v, u)) || r[u] == r[v] {
            return false;
        }
    }
    true
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let mut ans = 1;
    let g = read_graph(&mut inp);
    let mut r = vec![Relation::None; g.len()];
    for i in 0..r.len() {
        if r[i] == Relation::None {
            if !validate(&g, &mut r, i, i) {
                ans = 0;
                break;
            }
        }
    }
    println!("{ans}");
}
