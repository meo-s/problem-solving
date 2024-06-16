// https://www.acmicpc.net/problem/3107

use std::{
    collections::VecDeque,
    io::{stdin, Read},
};

fn main() {
    let mut inp = Default::default();
    stdin().lock().read_to_end(&mut inp).unwrap();

    let ipv6 = unsafe { String::from_utf8_unchecked(inp) };
    let mut ipv6 = ipv6.trim_end().split(':').enumerate();
    let mut groups = VecDeque::new();
    let mut emitted_group_start = 8;
    while let Some((i, group)) = ipv6.next() {
        if !group.is_empty() {
            groups.push_back(format!("{group:0>4}"));
        } else {
            emitted_group_start = emitted_group_start.min(i);
        }
    }

    let mut ans = String::new();
    let emitted_groups = 8 - groups.len();
    for _ in 0..emitted_group_start {
        ans.push_str(&format!("{}:", groups.pop_front().unwrap()));
    }
    for _ in 0..emitted_groups {
        ans.push_str("0000:");
    }
    while let Some(group) = groups.pop_front() {
        ans.push_str(&group);
        ans.push(':');
    }
    ans.pop();
    println!("{ans}");
}
