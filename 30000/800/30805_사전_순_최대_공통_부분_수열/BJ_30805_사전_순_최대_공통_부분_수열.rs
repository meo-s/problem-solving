// https://www.acmicpc.net/problem/30805

use std::{
    cmp::Ordering,
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
    let a: Vec<u32> = (0..scan!(inp, _)).map(|_| scan!(inp, _)).collect();
    let b: Vec<u32> = (0..scan!(inp, _)).map(|_| scan!(inp, _)).collect();

    let mut seq = vec![];
    for k in 0..b.len() {
        let start = seq
            .binary_search_by(|&i| {
                if a[i] < b[k] {
                    Ordering::Greater
                } else {
                    Ordering::Less
                }
            })
            .err()
            .map(|i| if 0 < i { Some(seq[i - 1] + 1) } else { None })
            .flatten()
            .unwrap_or(0);

        if let Some(i) = (start..a.len()).find(|&i| a[i] == b[k]) {
            while seq.last().is_some() && a[*seq.last().unwrap()] < b[k] {
                seq.pop();
            }
            seq.push(i);
        }
    }
    let mut ans = format!("{}\n", seq.len());
    seq.into_iter()
        .for_each(|i| ans.push_str(&format!("{} ", a[i])));
    if ans.as_bytes().last() == Some(&b' ') {
        ans.pop();
        ans.push('\n');
    }
    print!("{ans}");
}
