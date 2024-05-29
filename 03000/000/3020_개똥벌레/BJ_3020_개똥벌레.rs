// https://www.acmicpc.net/problem/3020

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

#[derive(Debug)]
enum Stone {
    Stalactite(u32),
    Stalagmite(u32),
}

impl Stone {
    fn unwrap(&self) -> u32 {
        match self {
            Stone::Stalactite(v) => *v,
            Stone::Stalagmite(v) => *v,
        }
    }
}

impl PartialEq for Stone {
    fn eq(&self, other: &Self) -> bool {
        self.unwrap() == other.unwrap()
    }
}

impl Eq for Stone {}

impl PartialOrd for Stone {
    fn lt(&self, other: &Self) -> bool {
        self.unwrap() < other.unwrap()
    }

    fn gt(&self, other: &Self) -> bool {
        other.unwrap() < self.unwrap()
    }

    fn partial_cmp(&self, other: &Self) -> Option<std::cmp::Ordering> {
        self.unwrap().partial_cmp(&other.unwrap())
    }
}

impl Ord for Stone {
    fn cmp(&self, other: &Self) -> std::cmp::Ordering {
        self.unwrap().cmp(&other.unwrap())
    }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let (n, h) = scan!(inp, _, u32);

    let mut stones = Vec::with_capacity(n);
    for _ in 0..n / 2 {
        stones.push(Stone::Stalagmite(scan!(inp, _)));
        stones.push(Stone::Stalactite(h - scan!(inp, u32)));
    }

    stones.sort_unstable();

    let mut ans = (n / 2, 1);
    let mut cur = n / 2;
    let mut it = stones.iter().rev().peekable();
    for y0 in (1..=h - 1).rev() {
        while let Some(&y) = it.peek() {
            if y0 != y.unwrap() {
                break;
            }
            if let Stone::Stalactite(..) = it.next().unwrap() {
                cur -= 1;
            } else {
                cur += 1;
            }
        }
        if ans.0 == cur {
            ans.1 += 1;
        } else if cur < ans.0 {
            ans = (cur, 1);
        }
    }

    println!("{} {}", ans.0, ans.1);
}
