// https://www.acmicpc.net/problem/1034

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
    };
}

fn main() {
    init!(inp);
    let mut inp = inp.split_ascii_whitespace();

    let mut states = HashMap::<_, usize>::new();
    let (n, _) = scan!(inp, usize, usize);
    for _ in 0..n {
        let state = inp.next().unwrap();
        states.insert(state, *states.get(&state).unwrap_or(&0) + 1);
    }

    let k = scan!(inp, _);
    let _fn = |&(state, _): &(&str, usize)| {
        let zeros = state.as_bytes().iter().filter(|&&v| v == b'0').count();
        zeros <= k && (k - zeros) % 2 == 0
    };

    let ans = states
        .into_iter()
        .filter(_fn)
        .map(|(_, count)| count)
        .max()
        .unwrap_or(0);
    println!("{ans}");
}
