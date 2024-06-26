// https://www.acmicpc.net/problem/4811

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

fn count(dp: &mut HashMap<(u8, u8), u64>, w: u8, h: u8) -> u64 {
    if let Some(&v) = dp.get(&(w, h)) {
        v
    } else {
        let v = if w == 0 {
            1
        } else if h == 0 {
            count(dp, w - 1, 1)
        } else {
            count(dp, w - 1, h + 1) + count(dp, w, h - 1)
        };
        dp.insert((w, h), v);
        v
    }
}

fn main() {
    init!(inp);
    let mut inp = inp.split_ascii_whitespace();

    let mut ans = String::default();
    let mut dp = HashMap::new();
    loop {
        let w = scan!(inp, _);
        if 0 < w {
            ans.push_str(&format!("{}\n", count(&mut dp, w, 0)));
        } else {
            print!("{ans}");
            break;
        }
    }
}
