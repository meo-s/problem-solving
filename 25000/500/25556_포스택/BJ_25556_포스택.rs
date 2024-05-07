// https://www.acmicpc.net/problem/25556

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

    let mut stacks = [i32::MIN; 4];
    for _ in 0..scan!(inp, _) {
        let n = scan!(inp, _);
        if let Some(stack) = stacks.iter_mut().find(|it| **it < n) {
            *stack = n;
        } else {
            println!("NO");
            return;
        }
    }
    println!("YES");
}
