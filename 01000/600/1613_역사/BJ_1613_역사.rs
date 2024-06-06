// https://www.acmicpc.net/problem/1613

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

    let (n, k) = scan!(inp, usize, usize);
    let mut g = vec![vec![u16::MAX as u32; n + 1]; n + 1];
    for _ in 0..k {
        let (u, v) = scan!(inp, usize, usize);
        g[u][v] = 1;
    }
    for w in 1..=n {
        for u in 1..=n {
            for v in 1..=n {
                g[u][v] = g[u][v].min(g[u][w] + g[w][v]);
            }
        }
    }

    let mut ans = String::new();
    for _ in 0..scan!(inp, _) {
        let (u, v) = scan!(inp, usize, usize);
        if g[u][v] < u16::MAX as u32 {
            ans.push_str("-1\n");
        } else if g[v][u] < u16::MAX as u32 {
            ans.push_str("1\n");
        } else {
            ans.push_str("0\n");
        }
    }

    print!("{ans}");
}
