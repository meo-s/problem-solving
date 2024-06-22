// https://www.acmicpc.net/problem/7490

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

fn brute_force(
    n_max: i32,
    n: i32,
    mut factor: i32,
    acc: i32,
    prev: String,
    cases: &mut Vec<String>,
) {
    if n_max < n {
        if acc == 0 {
            cases.push(prev);
        }
    } else {
        factor = factor * 10 + n;

        // ' '
        if n < n_max {
            brute_force(n_max, n + 1, factor, acc, prev.clone(), cases);
        }

        // '+'
        {
            let mut next = prev.clone();
            if 0 < prev.len() {
                next.push('+');
            }
            for ch in factor.to_string().chars() {
                next.push(ch);
                next.push(' ');
            }
            next.pop();
            brute_force(n_max, n + 1, 0, acc + factor, next, cases);
        }

        // '-'
        if 0 < prev.len() {
            let mut next = prev.clone();
            next.push('-');
            for ch in factor.to_string().chars() {
                next.push(ch);
                next.push(' ');
            }
            next.pop();
            brute_force(n_max, n + 1, 0, acc - factor, next, cases);
        }
    }
}

fn main() {
    init!(inp);
    let mut ans = String::new();
    for _ in 0..scan!(inp, _) {
        let mut cases = Default::default();
        brute_force(scan!(inp, _), 1, 0, 0, Default::default(), &mut cases);

        cases.sort_unstable();
        cases.drain(..).for_each(|s| {
            ans.push_str(&s);
            ans.push('\n');
        });
        ans.push('\n');
    }

    ans.pop();
    print!("{ans}");
}
