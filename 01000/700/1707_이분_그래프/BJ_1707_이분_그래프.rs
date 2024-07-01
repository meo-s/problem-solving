// https://www.acmicpc.net/problem/1707

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

macro_rules! init {
    ($name:ident) => {
        let mut $name = Default::default();
        stdin().lock().read_to_end(&mut $name).unwrap();
        let $name = unsafe { String::from_utf8_unchecked($name) };
    };
}

#[derive(Debug, Default, Clone, Copy, PartialEq, Eq)]
enum Marker {
    #[default]
    One,
    Two,
}

impl Marker {
    fn next(self) -> Marker {
        match self {
            Self::One => Self::Two,
            Self::Two => Self::One,
        }
    }
}

fn is_bipartite_graph(
    g: &Vec<Vec<usize>>,
    visited: &mut Vec<Option<Marker>>,
    u: usize,
    marker: Marker,
) -> bool {
    match visited[u] {
        Some(v) => v == marker,
        None => {
            visited[u] = Some(marker);
            g[u].iter()
                .all(|&v| is_bipartite_graph(g, visited, v, marker.next()))
        }
    }
}

fn main() {
    init!(inp);
    let mut inp = inp.split_ascii_whitespace();

    let mut ans = String::new();
    let mut visited = Vec::with_capacity(20_000);
    for _ in 0..scan!(inp, _) {
        let mut g = vec![vec![]; scan!(inp, _)];
        for _ in 0..scan!(inp, _) {
            let (u, v) = scan!(inp, usize, usize);
            g[u - 1].push(v - 1);
            g[v - 1].push(u - 1);
        }

        visited.resize(g.len(), None);
        visited.fill(None);
        ans.push_str(
            if (0..g.len()).all(|u| match visited[u] {
                None => is_bipartite_graph(&g, &mut visited, u, Default::default()),
                _ => true,
            }) {
                "YES\n"
            } else {
                "NO\n"
            },
        );
    }

    print!("{ans}");
}
