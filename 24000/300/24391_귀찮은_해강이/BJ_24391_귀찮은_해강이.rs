// https://www.acmicpc.net/problem/24391

use std::io::Read;

macro_rules! parse_next {
    ($it:expr) => {
        $it.next().unwrap().parse().unwrap()
    };
}

fn set_init(mut parents: Vec<usize>) -> Vec<usize> {
    (0..parents.len()).for_each(|it| parents[it] = it);
    parents
}

fn set_find(parents: &mut Vec<usize>, u: usize) -> usize {
    if parents[u] != u {
        parents[u] = set_find(parents, parents[u]);
    }
    parents[u]
}

fn set_merge(parents: &mut Vec<usize>, u: usize, v: usize) {
    let up = set_find(parents, u);
    let vp = set_find(parents, v);
    if up != vp {
        parents[vp] = up;
    }
}

fn read_inp() -> Vec<u8> {
    let mut inp = Vec::new();
    std::io::stdin().read_to_end(&mut inp).unwrap();
    inp
}

fn main() {
    let inp = read_inp();
    let inp = String::from_utf8_lossy(&inp);

    let mut it = inp.split_ascii_whitespace();
    let mut set = set_init(vec![0; parse_next!(it)]);
    for _ in 0..parse_next!(it) {
        let u: usize = parse_next!(it);
        let v: usize = parse_next!(it);
        set_merge(&mut set, u - 1, v - 1);
    }

    let mut ans = 0;
    let mut prev = None;
    for _ in 0..set.len() {
        let lec: usize = parse_next!(it);
        if let Some(prev) = prev {
            if set_find(&mut set, lec - 1) != set_find(&mut set, prev) {
                ans += 1;
            }
        }
        prev = Some(lec - 1);
    }
    println!("{ans}");
}
