// https://www.acmicpc.net/problem/1090

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

    macro_rules! take {
        ($it:expr, $e:tt) => {
            ($it).iter().map(|it| it.$e)
        };
    }

    let mut ans = String::new();
    let pawns: Vec<_> = (0..scan!(inp, _)).map(|_| scan!(inp, i32, i32)).collect();
    (1..=pawns.len())
        .map(|n| {
            take!(pawns, 0)
                .map(|x| {
                    take!(pawns, 1)
                        .map(|y| {
                            let mut distances: Vec<_> = pawns
                                .iter()
                                .map(|it| (it.0 - x).abs() + (it.1 - y).abs())
                                .collect();
                            distances.sort_unstable();
                            distances.into_iter().take(n).sum::<i32>()
                        })
                        .min()
                        .unwrap()
                })
                .min()
                .unwrap()
        })
        .for_each(|dist| ans.push_str(&format!("{dist} ")));
    println!("{ans}");
}
