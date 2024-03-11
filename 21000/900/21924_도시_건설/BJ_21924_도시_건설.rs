// https://www.acmicpc.net/problem/21924

#![allow(unused_must_use)]

use std::io::Read;
use std::str::{self, FromStr};

fn scan<'a, T>(it: &mut impl std::iter::Iterator<Item = &'a str>) -> T
where
    T: FromStr,
{
    unsafe { str::parse::<T>(it.next().unwrap()).unwrap_unchecked() }
}

fn find(parents: &mut Vec<usize>, u: usize) -> usize {
    if parents[u] != u {
        parents[u] = find(parents, parents[u]);
    }
    parents[u]
}

fn merge(parents: &mut Vec<usize>, u: usize, v: usize) -> bool {
    let up = find(parents, u);
    let vp = find(parents, v);
    if up != vp {
        parents[up] = vp;
    }
    up != vp
}

fn main() {
    let mut inp = vec![];
    std::io::stdin().read_to_end(&mut inp);

    let mut it = unsafe { str::from_utf8_unchecked(&inp) }.split_whitespace();
    let n = scan::<usize>(&mut it);
    let m = scan::<usize>(&mut it);

    let mut edges = vec![];
    let mut prev_cost = 0;
    (0..m).for_each(|_| {
        let a = scan::<usize>(&mut it) - 1;
        let b = scan::<usize>(&mut it) - 1;
        let c = scan::<i64>(&mut it);
        prev_cost += c;
        edges.push((c, (a, b)));
    });

    let mut parents = (0..n).collect::<Vec<usize>>();
    let mut num_conns = 0;
    let mut cost = 0;
    edges.sort_unstable_by_key(|(c, _)| *c);
    edges.iter().any(|(c, (a, b))| {
        if merge(&mut parents, *a, *b) {
            num_conns += 1;
            cost += c;
        }
        num_conns == n - 1
    });
    println!(
        "{:?}",
        if num_conns == n - 1 {
            prev_cost - cost
        } else {
            -1
        }
    );
}
