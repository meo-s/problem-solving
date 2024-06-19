// https://www.acmicpc.net/problem/2573

use std::{
    collections::VecDeque,
    io::{stdin, Read},
    sync::OnceLock,
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
        let mut $name = $name.split_ascii_whitespace();
    };
}

static DIRECTIONS: [(usize, usize); 4] = [(usize::MAX, 0), (0, usize::MAX), (1, 0), (0, 1)];

fn neighbors<'a>(
    (h, w): (usize, usize),
    pt0: (usize, usize),
    directions: &'a [(usize, usize)],
) -> (impl Iterator<Item = (usize, usize)> + 'a) {
    directions
        .iter()
        .map(move |&(dy, dx)| (pt0.0.wrapping_add(dy), pt0.1.wrapping_add(dx)))
        .filter(move |&(ny, nx)| ny < h && nx < w)
}

fn dfs(g: &Vec<Vec<i8>>, visited: &mut Vec<Vec<bool>>, y: usize, x: usize) -> bool {
    if visited[y][x] || g[y][x] <= 0 {
        false
    } else {
        visited[y][x] = true;
        for (ny, nx) in neighbors((g.len(), g[0].len()), (y, x), &DIRECTIONS) {
            dfs(g, visited, ny, nx);
        }
        true
    }
}

fn icebergs(g: &Vec<Vec<i8>>) -> usize {
    static mut VISITED: OnceLock<Vec<Vec<bool>>> = OnceLock::new();
    unsafe { VISITED.get_or_init(|| vec![vec![false; g[0].len()]; g.len()]) };

    let visited = unsafe { VISITED.get_mut().unwrap() };
    visited.iter_mut().for_each(|it| it.fill(false));

    (0..g.len())
        .map(|y| {
            (0..g[0].len())
                .map(|x| dfs(g, visited, y, x))
                .filter(|&v| v)
                .count()
        })
        .sum::<usize>()
}

fn main() {
    init!(inp);
    let (h, w) = scan!(inp, _, _);
    let mut g: Vec<Vec<_>> = (0..h)
        .map(|_| (0..w).map(|_| scan!(inp, i8)).collect())
        .collect();

    let mut zeros = VecDeque::new();
    for y in 0..h {
        for x in 0..w {
            if g[y][x] == 0 && neighbors((h, w), (y, x), &DIRECTIONS).any(|(ny, nx)| 0 < g[ny][nx])
            {
                zeros.push_back((y, x));
            }
        }
    }

    for t in 1.. {
        for _ in 0..zeros.len() {
            let pt = zeros.pop_front().unwrap();
            zeros.push_back(pt);
            for (ny, nx) in neighbors((h, w), pt, &DIRECTIONS) {
                if 0 < g[ny][nx] {
                    g[ny][nx] -= 1;
                    if g[ny][nx] == 0 {
                        zeros.push_back((ny, nx));
                    }
                }
            }
        }
        for _ in 0..zeros.len() {
            let pt = zeros.pop_front().unwrap();
            if neighbors((h, w), pt, &DIRECTIONS).any(|(ny, nx)| 0 < g[ny][nx]) {
                zeros.push_back(pt);
            }
        }

        if 1 < icebergs(&g) {
            println!("{t}");
            return;
        }
        if zeros.is_empty() {
            println!("0");
            return;
        }
    }
}
