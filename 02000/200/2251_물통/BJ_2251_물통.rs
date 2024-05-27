// https://www.acmicpc.net/problem/2251

use std::{
    collections::HashSet,
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
    let mut buf = Default::default();
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn visit(buckets: &[u8; 3], state: [u8; 3], visited: &mut HashSet<[u8; 3]>, ans: &mut [bool]) {
    if !visited.contains(&state) {
        visited.insert(state);

        for i in (0..3).filter(|it| 0 < state[*it]) {
            for j in (0..3).filter(|it| *it != i && state[*it] < buckets[*it]) {
                let dv = state[i].min(buckets[j] - state[j]);
                let mut next = state.clone();
                next[i] -= dv;
                next[j] += dv;
                visit(buckets, next, visited, ans);
            }
        }

        if state[0] == 0 {
            ans[state[2] as usize] = true;
        }
    }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let (a, b, c) = scan!(inp, _, _, _);

    let mut ans = vec![false; 201];
    let mut visited = HashSet::new();
    visit(&[a, b, c], [0, 0, c], &mut visited, &mut ans);

    let ans: String = ans
        .iter()
        .enumerate()
        .filter(|&(_, visited)| *visited)
        .fold(Default::default(), |mut acc, (v, _)| {
            acc.push_str(&format!("{v} "));
            acc
        });
    println!("{ans}");
}
