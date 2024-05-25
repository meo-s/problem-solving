// https://www.acmicpc.net/problem/1937

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

fn read_input() -> String {
    let mut buf = Vec::with_capacity(1 << 20);
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn dfs(forest: &Vec<Vec<u32>>, dp: &mut Vec<Vec<Option<u32>>>, y: usize, x: usize) -> u32 {
    if dp[y][x].is_none() {
        dp[y][x] = 1.into();
        for (dy, dx) in [(0, 1), (usize::MAX, 0), (0, usize::MAX), (1, 0)] {
            let ny = y.wrapping_add(dy);
            let nx = x.wrapping_add(dx);
            if ny < forest.len() && nx < forest.len() && forest[y][x] < forest[ny][nx] {
                let v = dfs(forest, dp, ny, nx) + 1;
                dp[y][x] = dp[y][x].map_or(v, |v0| v0.max(v)).into();
            }
        }
    }
    dp[y][x].unwrap()
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let n = scan!(inp, _);
    let forest: Vec<Vec<u32>> = (0..n)
        .map(|_| (0..n).map(|_| scan!(inp, _)).collect())
        .collect();

    let mut ans = 0;
    let mut dp = vec![vec![None; n]; n];
    for y in 0..n {
        ans = ans.max((0..n).map(|x| dfs(&forest, &mut dp, y, x)).max().unwrap());
    }

    println!("{ans}");
}
