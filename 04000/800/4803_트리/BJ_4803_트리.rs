// https://www.acmicpc.net/problem/4803

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

fn read_input() -> String {
    let mut buf = Vec::with_capacity(1 << 20);
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn read_graph<'a>(inp: &mut impl Iterator<Item = &'a str>) -> Option<Graph> {
    let (n, m) = scan!(inp, _, _);
    if n == 0 && m == 0 {
        None
    } else {
        let mut g = vec![vec![]; n];
        for _ in 0..m {
            let (u, v) = scan!(inp, usize, usize);
            g[u - 1].push(v - 1);
            g[v - 1].push(u - 1);
        }
        Some(g)
    }
}

fn is_tree(g: &Graph, visited: &mut [bool], u: usize, p: usize) -> bool {
    if visited[u] {
        false
    } else {
        visited[u] = true;
        g[u].iter()
            .filter(|&&it| it != p)
            .all(|&it| is_tree(g, visited, it, u))
    }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let mut idx = 1;
    let mut ans = String::new();
    while let Some(g) = read_graph(&mut inp) {
        let mut visited = vec![false; g.len()];
        match (0..g.len())
            .map(|it| is_tree(&g, &mut visited, it, it))
            .filter(|&it| it)
            .count()
        {
            0 => ans.push_str(&format!("Case {idx}: No trees.\n")),
            1 => ans.push_str(&format!("Case {idx}: There is one tree.\n")),
            n @ _ => ans.push_str(&format!("Case {idx}: A forest of {n} trees.\n")),
        }
        idx += 1;
    }
    print!("{ans}");
}
