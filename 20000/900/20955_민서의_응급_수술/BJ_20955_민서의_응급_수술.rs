// https://www.acmicpc.net/problem/20955

use std::io::Read;

struct DisjointSet(Vec<usize>);

impl DisjointSet {
    fn new(n: usize) -> Self {
        Self((0..n).collect())
    }

    fn find(&mut self, u: usize) -> usize {
        if self.0[u] != u {
            self.0[u] = self.find(self.0[u]);
        }
        self.0[u]
    }

    fn merge(&mut self, u: usize, v: usize) -> bool {
        let up = self.find(u);
        let vp = self.find(v);
        if up != vp {
            self.0[vp] = up;
        }
        up != vp
    }
}

fn main() {
    let mut inp = Vec::new();
    std::io::stdin().lock().read_to_end(&mut inp).unwrap();

    let inp = String::from_utf8_lossy(&inp);
    let mut it = inp.split_ascii_whitespace();

    let mut ans = 0;
    let n = it.next().unwrap().parse().unwrap();
    let m = it.next().unwrap().parse().unwrap();
    let mut disjoint_set = DisjointSet::new(n);
    for _ in 0_usize..m {
        let u: usize = it.next().unwrap().parse().unwrap();
        let v: usize = it.next().unwrap().parse().unwrap();
        if !disjoint_set.merge(u - 1, v - 1) {
            ans += 1;
        }
    }
    for i in 1..n {
        if disjoint_set.merge(0, i) {
            ans += 1;
        }
    }
    println!("{ans}");
}
