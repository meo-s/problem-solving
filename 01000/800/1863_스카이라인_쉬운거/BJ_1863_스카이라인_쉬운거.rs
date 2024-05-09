// https://www.acmicpc.net/problem/1863

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
    let mut buf = Vec::with_capacity(1 << 18);
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let mut ans = 0;
    let mut buildings = vec![0];
    for _ in 0..scan!(inp, _) {
        inp.next();
        let it = scan!(inp, i32);
        while it < *buildings.last().unwrap() {
            buildings.pop();
        }
        if it != *buildings.last().unwrap() {
            buildings.push(it);
            ans += 1;
        }
    }
    println!("{ans}");
}
