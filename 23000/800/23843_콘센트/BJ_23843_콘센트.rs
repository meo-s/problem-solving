// https://www.acmicpc.net/problem/23843

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
    let mut buf = Vec::with_capacity(1 << 18);
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let (n, m) = scan!(inp, usize, usize);
    let mut times = (0..n).map(|_| scan!(inp, i32)).collect::<Vec<_>>();
    times.sort_unstable();

    let mut concents = BinaryHeap::from_iter(vec![0; m]);
    for dt in times.iter().rev() {
        let t = -concents.pop().unwrap();
        concents.push(-(t + dt));
    }

    println!("{}", -concents.drain().min().unwrap());
}
