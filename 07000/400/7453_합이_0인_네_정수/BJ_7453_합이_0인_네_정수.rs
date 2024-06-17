// https://www.acmicpc.net/problem/7453

use std::{
    collections::HashMap,
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

macro_rules! init {
    ($name:ident) => {
        let mut $name = Default::default();
        stdin().lock().read_to_end(&mut $name).unwrap();
        let $name = unsafe { String::from_utf8_unchecked($name) };
        let mut $name = $name.split_ascii_whitespace();
    };
}

fn main() {
    init!(inp);

    let n = scan!(inp, _);
    let mut a = vec![[0, 0, 0, 0]; n];
    for i in 0..a.len() {
        (0..4).for_each(|j| a[i][j] = scan!(inp, _));
    }

    let mut counter = HashMap::new();
    for i in 0..n {
        for j in 0..n {
            let v = a[i][2] + a[j][3];
            counter.insert(v, counter.get(&v).unwrap_or(&0) + 1);
        }
    }

    let mut ans = 0usize;
    for i in 0..n {
        for j in 0..n {
            let v = a[i][0] + a[j][1];
            ans += counter.get(&-v).unwrap_or(&0);
        }
    }

    println!("{ans}");
}
