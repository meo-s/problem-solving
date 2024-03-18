// https://www.acmicpc.net/problem/10828

use std::{io::Read, str::FromStr};

fn scan<'a, T: FromStr>(it: &mut impl Iterator<Item = &'a str>) -> T {
    unsafe { str::parse::<T>(it.next().unwrap()).unwrap_unchecked() }
}

fn main() {
    let mut inp = vec![];
    std::io::stdin()
        .read_to_end(&mut inp)
        .expect("failed to read input");

    let mut it = unsafe { std::str::from_utf8_unchecked(&inp) }
        .trim_end()
        .split_whitespace();

    let mut ans = String::new();
    let mut st = std::collections::LinkedList::<i32>::new();
    for _ in 0..scan::<usize>(&mut it) {
        match it.next().unwrap() {
            "top" => {
                if let Some(v) = st.back() {
                    ans.push_str(&v.to_string());
                    ans.push('\n');
                } else {
                    ans.push_str("-1\n");
                }
            }
            "size" => {
                ans.push_str(&st.len().to_string());
                ans.push('\n');
            }
            "empty" => {
                ans.push_str(if st.is_empty() { "1\n" } else { "0\n" });
            }
            "pop" => {
                if let Some(v) = st.pop_back() {
                    ans.push_str(&v.to_string());
                    ans.push('\n');
                } else {
                    ans.push_str("-1\n");
                }
            }
            "push" => st.push_back(scan::<i32>(&mut it)),
            _ => unreachable!(),
        }
    }

    println!("{ans}");
}
