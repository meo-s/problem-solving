// https://www.acmicpc.net/problem/1374

use std::{
    cmp::Reverse,
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
    let mut buf = Vec::with_capacity(1 << 20);
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let mut lectures = vec![];
    for _ in 0..scan!(inp, _) {
        let (_, s, e) = scan!(inp, u32, u32, u32);
        lectures.push((s, e));
    }
    lectures.sort_unstable_by_key(|v| Reverse(v.0));

    let mut rooms = BinaryHeap::from_iter([Reverse(0)]);
    while let Some((s, e)) = lectures.pop() {
        if rooms.peek().unwrap().0 <= s {
            rooms.pop();
            rooms.push(Reverse(e));
        } else {
            rooms.push(Reverse(e));
        }
    }
    println!("{}", rooms.len());
}
