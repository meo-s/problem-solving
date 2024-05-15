// https://www.acmicpc.net/problem/14658

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

fn count_meteors_in_rect(meteors: &[(i32, i32)], x0: i32, y0: i32, l: i32) -> usize {
    meteors
        .iter()
        .filter(|&&(x, y)| x0 <= x && x <= x0 + l && y0 <= y && y <= y0 + l)
        .count()
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace().skip(2);
    let (l, k) = scan!(inp, _, _);
    let meteors: Vec<_> = (0..k).map(|_| scan!(inp, _, _)).collect();
    let ans = meteors.len()
        - meteors
            .iter()
            .map(|&it| it.0)
            .map(|x| {
                meteors
                    .iter()
                    .map(|&it| it.1)
                    .map(|y| count_meteors_in_rect(&meteors, x, y, l))
                    .max()
                    .unwrap()
            })
            .max()
            .unwrap();
    println!("{ans}");
}
