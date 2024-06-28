// https://www.acmicpc.net/problem/1117

use std::{
    cmp::{max, min},
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
    };
}

fn main() {
    init!(inp);
    let mut inp = inp.split_ascii_whitespace();

    let (w, h, f, c, x1, y1, x2, y2) = scan!(inp, i64, i64, i64, i64, i64, i64, i64, i64);
    let ans = (h * w)
        - max(min(x2, min(f, w - f)) - x1, 0) * (y2 - y1) * 2 * (c + 1)
        - max(x2 - max(x1, min(f, w - f)), 0) * (y2 - y1) * (c + 1);
    println!("{ans}");
}
