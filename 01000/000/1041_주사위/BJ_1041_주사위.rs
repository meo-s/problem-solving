// https://www.acmicpc.net/problem/1041

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
    let n = scan!(inp, u64);
    let dice = Vec::<u64>::from_iter(inp.map(|it| it.parse().unwrap()));
    match n {
        1 => println!("{}", dice.iter().sum::<u64>() - dice.iter().max().unwrap()),
        n @ _ => {
            let mut v = [
                dice[0].min(dice[5]),
                dice[1].min(dice[4]),
                dice[2].min(dice[3]),
            ];
            v.sort_unstable();
            println!(
                "{}",
                (v[0] + v[1] + v[2]) * 4
                    + (v[0] + v[1]) * (8 * (n - 2) + 4)
                    + v[0] * (5 * (n - 2).pow(2) + 4 * (n - 2))
            );
        }
    }
}
