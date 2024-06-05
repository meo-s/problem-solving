// https://www.acmicpc.net/problem/1052

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

    let (mut n, k) = scan!(inp, usize, usize);
    let mut bottles = vec![];
    for sz in (0..).map(|v| 1u64 << v) {
        if 0 < n % 2 {
            bottles.push(sz);
        }
        n /= 2;
        if n == 0 {
            break;
        }
    }

    let mut ans = 0;
    bottles.reverse();
    while k < bottles.len() {
        ans += bottles.last().unwrap();
        bottles.push(*bottles.last().unwrap());
        while 1 < bottles.len() && bottles[bottles.len() - 1] == bottles[bottles.len() - 2] {
            bottles.pop();
            *bottles.last_mut().unwrap() *= 2;
        }
    }

    println!("{ans}");
}
