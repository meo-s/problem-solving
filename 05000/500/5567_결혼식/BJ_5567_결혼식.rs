// https://www.acmicpc.net/problem/5567

fn scan<'a, T: std::str::FromStr>(it: &mut impl Iterator<Item = &'a str>) -> T {
    unsafe { str::parse::<T>(it.next().unwrap()).unwrap_unchecked() }
}

fn main() {
    #![allow(unused_must_use)]
    use std::io::Read;

    let mut inp = vec![];
    std::io::stdin().read_to_end(&mut inp);

    let it = &mut unsafe { std::str::from_utf8_unchecked(&inp) }
        .trim_end()
        .split_ascii_whitespace();

    let n = scan::<usize>(it);
    let m = scan::<usize>(it);

    let mut g = vec![vec![]; n];
    (0..m).for_each(|_| {
        let u = scan::<usize>(it) - 1;
        let v = scan::<usize>(it) - 1;
        g[u].push(v);
        g[v].push(u);
    });

    let mut dists = vec![u32::MAX; n];
    dists[0] = 0;
    for &u in &g[0] {
        dists[u as usize] = 1;
        for &v in &g[u as usize] {
            dists[v as usize] = 2;
        }
    }

    let ans = dists.iter().skip(1).filter(|&v| *v != u32::MAX).count();
    println!("{ans}");
}
