// https://www.acmicpc.net/problem/13164

use std::{
    collections::BinaryHeap,
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

fn main() {
    init!(inp);

    let (n, k) = scan!(inp, _, usize);
    let kids: Vec<u32> = (0..n).map(|_| scan!(inp, _)).collect();

    let mut diffs = BinaryHeap::new();
    for v in kids.windows(2) {
        if diffs.len() < n - k {
            diffs.push(v[1] - v[0]);
        } else if !diffs.is_empty() {
            if v[1] - v[0] < *diffs.peek().unwrap() {
                diffs.pop();
                diffs.push(v[1] - v[0]);
            }
        }
    }

    println!("{}", diffs.drain().sum::<u32>());
}
