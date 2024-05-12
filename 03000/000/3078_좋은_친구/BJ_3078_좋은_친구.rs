// https://www.acmicpc.net/problem/3078

use std::{
    collections::VecDeque,
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

fn read_input() -> String {
    let mut buf = Vec::with_capacity(1 << 18);
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn count_friends(people: &mut VecDeque<usize>, names: &mut [usize]) -> usize {
    let name = people.pop_front().unwrap();
    names[name] -= 1;
    return names[name];
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let mut ans = 0;
    let (n, k) = scan!(inp, usize, usize);
    let mut people = VecDeque::new();
    let mut names = vec![0; 21];
    for _ in 0..n {
        if people.len() == k + 1 {
            ans += count_friends(&mut people, names.as_mut_slice());
        }
        people.push_back(inp.next().unwrap().len());
        names[*people.back().unwrap()] += 1;
    }
    while !people.is_empty() {
        ans += count_friends(&mut people, names.as_mut_slice());
    }
    println!("{ans}");
}
