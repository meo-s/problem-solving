// https://www.acmicpc.net/problem/3178

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

fn count_vertices(s: &[String]) -> usize {
    let mut count = s[0].len();
    for (c, n) in s[..s.len() - 1].iter().zip(s[1..].iter()) {
        count += s[0].len()
            - c.as_bytes()
                .iter()
                .zip(n.as_bytes().iter())
                .take_while(|ch| ch.0 == ch.1)
                .count();
    }
    count
}

fn main() {
    init!(inp);
    let (_, k) = scan!(inp, usize, usize);
    let mut prefixes = vec![];
    let mut suffixes: Vec<String> = vec![];
    while let Some(s) = inp.next() {
        prefixes.push(s[..k].to_string());
        suffixes.push(s[k..].chars().rev().collect());
    }

    prefixes.sort_unstable();
    suffixes.sort_unstable();
    println!("{}", count_vertices(&prefixes) + count_vertices(&suffixes));
}
