// https://www.acmicpc.net/problem/9011

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

fn main() {
    init!(inp);
    let mut ans = String::new();
    for _ in 0..scan!(inp, _) {
        let seq: Vec<_> = (0..scan!(inp, _)).map(|_| scan!(inp, _)).collect();

        let mut n: Vec<_> = (0..seq.len()).collect();
        let mut st: Vec<_> = Default::default();
        for &ri in seq.iter().rev() {
            if n.len() <= ri {
                ans.push_str("IMPOSSIBLE\n");
                break;
            }
            st.push(n.remove(ri));
        }
        if n.is_empty() {
            while !st.is_empty() {
                ans.push_str(&format!("{} ", st.pop().unwrap() + 1));
            }
            ans.pop();
            ans.push('\n');
        }
    }

    print!("{ans}");
}
