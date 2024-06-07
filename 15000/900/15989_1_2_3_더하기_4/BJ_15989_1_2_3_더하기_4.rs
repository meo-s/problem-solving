// https://www.acmicpc.net/problem/15989

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
    let mut buf = Default::default();
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let mut dp = vec![0u64; 10_001];
    dp[0] = 1;
    dp[1] = 1;
    dp[2] = 2;
    for i in 3..dp.len() {
        dp[i] = dp[i - 3] + (i as u64 / 2) + 1;
    }

    let mut ans = String::new();
    for _ in 0..scan!(inp, _) {
        ans.push_str(&format!("{}\n", dp[scan!(inp, usize)]));
    }

    print!("{ans}");
}
