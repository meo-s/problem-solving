// https://www.acmicpc.net/problem/1516

use std::{collections::BinaryHeap, io::Read};

macro_rules! parse_next {
    ($it:expr) => {
        $it.next().unwrap().parse().unwrap()
    };
}

fn read_inp() -> Vec<u8> {
    let mut inp = Vec::new();
    std::io::stdin().read_to_end(&mut inp).unwrap();
    inp
}

fn main() {
    let inp = read_inp();
    let inp = String::from_utf8_lossy(&inp);

    let mut tokens = inp.split_ascii_whitespace();
    let n = parse_next!(tokens);
    let mut t = Vec::<i32>::with_capacity(n);
    let mut dep_count = vec![0; n];
    let mut dep_graph = vec![vec![]; n];
    for u in 0..n {
        t.push(parse_next!(tokens));
        tokens
            .by_ref()
            .take_while(|&token| token != "-1")
            .for_each(|token| {
                let v = token.parse::<usize>().unwrap() - 1;
                dep_count[u] += 1;
                dep_graph[v].push(u);
            });
    }

    let mut times = vec![0; n];
    let mut pending = BinaryHeap::with_capacity(n);
    for (u, count) in dep_count.iter().enumerate() {
        if *count == 0 {
            pending.push((-t[u], u));
        }
    }
    while !pending.is_empty() {
        let (now, u) = pending.pop().unwrap();
        times[u] = -now;
        for &v in &dep_graph[u] {
            dep_count[v] -= 1;
            if dep_count[v] == 0 {
                pending.push((now - t[v], v));
            }
        }
    }

    let mut ans = String::with_capacity(4 * n);
    for time in times {
        ans.push_str(&time.to_string());
        ans.push('\n');
    }
    println!("{ans}");
}
