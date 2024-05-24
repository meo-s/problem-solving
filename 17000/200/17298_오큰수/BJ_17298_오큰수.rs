// https://www.acmicpc.net/problem/17298

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
    let mut buf = Vec::with_capacity(1 << 20);
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let mut nge = vec![-1; scan!(inp, _)];
    let mut pending = vec![];
    for (i, n) in inp.map(|it| it.parse::<i32>().unwrap()).enumerate() {
        while let Some(&(v, idx)) = pending.last() {
            if n <= v {
                break;
            }
            nge[idx] = n;
            pending.pop();
        }
        pending.push((n, i));
    }

    let mut ans = String::new();
    nge.iter().for_each(|v| ans.push_str(&format!("{v} ")));
    println!("{ans}");
}
