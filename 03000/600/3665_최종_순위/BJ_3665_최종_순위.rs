// https://www.acmicpc.net/problem/3665

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

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let mut ans = String::new();
    for _ in 0..scan!(inp, _) {
        let n = scan!(inp, _);
        let mut prev_ranks = vec![0; n];
        (0..prev_ranks.len()).for_each(|it| prev_ranks[scan!(inp, usize) - 1] = it);

        let mut ranks = prev_ranks.clone();
        for _ in 0..scan!(inp, _) {
            let (u, v) = scan!(inp, usize, usize);
            if prev_ranks[u - 1] < prev_ranks[v - 1] {
                ranks[u - 1] += 1;
                ranks[v - 1] -= 1;
            } else {
                ranks[u - 1] -= 1;
                ranks[v - 1] += 1;
            }
        }

        let mut dashboard: Vec<_> = ranks.into_iter().zip(1..=n).collect();
        dashboard.sort_unstable();
        if dashboard
            .iter()
            .zip(0..n)
            .any(|(&(team_rank, _), i)| i < team_rank)
        {
            ans.push_str("IMPOSSIBLE\n");
            continue;
        }
        if dashboard
            .iter()
            .zip(0..n)
            .any(|(&(team_rank, _), i)| i != team_rank)
        {
            ans.push_str("?\n");
            continue;
        }
        for (_, team_no) in dashboard {
            ans.push_str(&format!("{team_no} "));
        }
        ans.push('\n');
    }
    println!("{ans}");
}
