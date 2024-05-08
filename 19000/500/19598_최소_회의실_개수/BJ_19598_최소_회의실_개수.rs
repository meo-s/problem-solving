// https://www.acmicpc.net/problem/19598

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

fn read_input() -> String {
    let mut buf = Default::default();
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let n = scan!(inp, _);
    let mut meetings = Vec::with_capacity(n);
    (0..n).for_each(|_| meetings.push(scan!(inp, i32, i32)));
    meetings.sort_unstable();

    let mut rooms = BinaryHeap::from_iter([-meetings[0].1]);
    for &(s, e) in meetings.iter().skip(1) {
        if -*rooms.peek().unwrap() <= s {
            rooms.pop();
        }
        rooms.push(-e);
    }
    println!("{}", rooms.len());
}
