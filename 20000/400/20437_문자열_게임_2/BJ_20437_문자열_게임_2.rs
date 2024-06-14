// https://www.acmicpc.net/problem/20437

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
    let mut ans = String::new();
    for _ in 0..scan!(inp, _) {
        let (s, k) = (inp.next().unwrap(), scan!(inp, _));
        if k == 1 {
            ans.push_str("1 1\n");
            continue;
        }

        let mut all_indices: HashMap<_, _> = (b'a'..=b'z').map(|ch| (ch, vec![])).collect();
        for (i, ch) in s.as_bytes().iter().enumerate() {
            all_indices.get_mut(ch).unwrap().push(i);
        }

        let lens: Vec<_> = all_indices
            .iter()
            .filter(|(_, indices)| k <= indices.len())
            .map(|(_, indices)| {
                let (mut min_len, mut max_len) = (usize::MAX, usize::MIN);
                let mut iter = indices.windows(k);
                while let Some([s, .., e]) = iter.next() {
                    min_len = min_len.min(e - s + 1);
                    max_len = max_len.max(e - s + 1);
                }
                (min_len, max_len)
            })
            .collect();

        if lens.is_empty() {
            ans.push_str("-1\n");
        } else {
            let min_len = lens.iter().map(|(v, _)| v).min().unwrap();
            let max_len = lens.iter().map(|(_, v)| v).max().unwrap();
            ans.push_str(&format!("{min_len} {max_len}\n"));
        }
    }

    print!("{ans}");
}
