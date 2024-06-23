// https://www.acmicpc.net/problem/2660

use std::{
    collections::VecDeque,
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
        let mut $name = $name.split_ascii_whitespace();
    };
}

type Graph = Vec<Vec<usize>>;

trait ScoreOf {
    fn score_of(&self, u: usize) -> u32;
}

impl ScoreOf for Graph {
    fn score_of(&self, u: usize) -> u32 {
        let mut score = u32::MAX;
        let mut pending = VecDeque::from([u]);
        let mut visited = vec![false; self.len()];
        visited[u] = true;
        while !pending.is_empty() {
            score = score.wrapping_add(1);
            for _ in 0..pending.len() {
                let u = pending.pop_front().unwrap();
                for &v in self.get(u).unwrap() {
                    if !visited[v] {
                        visited[v] = true;
                        pending.push_back(v);
                    }
                }
            }
        }

        score
    }
}

fn main() {
    init!(inp);

    let n = scan!(inp, _);
    let mut g = vec![vec![]; n];
    loop {
        let (mut u, mut v) = scan!(inp, i32, i32);
        if (u, v) == (-1, -1) {
            break;
        }

        u -= 1;
        v -= 1;
        g[u as usize].push(v as usize);
        g[v as usize].push(u as usize);
    }

    let scores: Vec<_> = (0..n).map(|u| g.score_of(u)).collect();
    let min_score = scores.iter().min().unwrap();
    let candidates: Vec<_> = scores
        .iter()
        .enumerate()
        .filter(|it| *min_score != u32::MAX && it.1 == min_score)
        .collect();

    let mut ans = String::new();
    ans.push_str(&format!("{} {}\n", min_score, candidates.len()));
    for (no, _) in candidates {
        ans.push_str(&format!("{} ", no + 1));
    }

    ans.pop();
    println!("{ans}");
}
