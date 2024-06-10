// https://www.acmicpc.net/problem/16987

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

macro_rules! init {
    ($name:ident) => {
        let mut $name = Default::default();
        stdin().lock().read_to_end(&mut $name).unwrap();
        let $name = unsafe { String::from_utf8_unchecked($name) };
        let mut $name = $name.split_ascii_whitespace();
    };
}

fn brute_force(eggs: &mut [(i32, i32)], at: usize) -> usize {
    if at == eggs.len() || eggs.iter().filter(|it| 0 < it.0).count() <= 1 {
        eggs.iter().filter(|it| it.0 <= 0).count()
    } else {
        if eggs[at].0 <= 0 {
            brute_force(eggs, at + 1)
        } else {
            let mut max_count = 0;
            for i in (0..eggs.len()).filter(|&it| it != at) {
                if 0 < eggs[i].0 {
                    eggs[at].0 -= eggs[i].1;
                    eggs[i].0 -= eggs[at].1;
                    max_count = max_count.max(brute_force(eggs, at + 1));
                    eggs[at].0 += eggs[i].1;
                    eggs[i].0 += eggs[at].1;
                }
            }
            max_count
        }
    }
}

fn main() {
    init!(inp);
    let n = scan!(inp, _);
    let mut eggs: Vec<_> = (0..n).map(|_| scan!(inp, _, _)).collect();
    println!("{}", brute_force(&mut eggs, 0));
}
