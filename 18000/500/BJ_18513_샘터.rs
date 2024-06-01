// https://www.acmicpc.net/problem/18513

use std::{
    collections::{HashMap, VecDeque},
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

    let (n, mut k) = scan!(inp, usize, usize);
    let mut sites = VecDeque::new();
    let mut visited = HashMap::new();
    for _ in 0..n {
        let x = scan!(inp, i32);
        sites.push_back(x);
        visited.insert(x, 0u32);
    }

    let mut ans = 0u64;
    while 0 < k {
        let x = sites.pop_front().unwrap();
        let w = visited[&x];
        for dx in [-1, 1] {
            if 0 < k && !visited.contains_key(&(x + dx)) {
                k -= 1;
                ans += (w + 1) as u64;
                visited.insert(x + dx, w + 1);
                sites.push_back(x + dx);
            }
        }
    }

    println!("{ans}");
}
