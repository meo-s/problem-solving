// https://www.acmicpc.net/problem/2866

use std::{
    collections::HashSet,
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

    let (r, c) = scan!(inp, _, _);
    let mut table: Vec<_> = (0..c).map(|_| String::with_capacity(r)).collect();
    for line in inp {
        let line = line.as_bytes();
        for i in 0..c {
            table[i].push(line[i] as char);
        }
    }

    let mut ans = 0;
    let (mut lb, mut ub) = (0, r);
    while lb < ub {
        let mid = (lb + ub) / 2;
        let mut pool = HashSet::new();
        if table.iter().all(|it| pool.insert(&it[mid..])) {
            ans = ans.max(mid);
            lb = mid + 1;
        } else {
            ub = mid;
        }
    }

    println!("{ans}");
}
