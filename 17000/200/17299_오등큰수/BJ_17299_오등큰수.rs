// https://www.acmicpc.net/problem/17299

use std::str::FromStr;

fn scan<'a, T: FromStr>(it: &mut impl Iterator<Item = &'a str>) -> T {
    unsafe { str::parse::<T>(it.next().unwrap_unchecked()).unwrap_unchecked() }
}

fn main() {
    #![allow(unused_must_use)]
    use std::io::{Read, Write};

    let mut inp = vec![];
    std::io::stdin().read_to_end(&mut inp);

    let it = &mut unsafe { std::str::from_utf8_unchecked(&inp) }
        .trim_end()
        .split_ascii_whitespace();

    let seq_len = scan::<usize>(it);
    let mut counter = [0; 1_000_001];
    let mut seq = vec![];
    seq.reserve_exact(seq_len);
    (0..seq_len).for_each(|_| {
        seq.push(scan::<i32>(it));
        counter[*seq.last().unwrap() as usize] += 1;
    });

    let mut ans = vec![-1; seq_len];
    let mut hist = vec![];
    seq.iter().enumerate().rev().for_each(|(i, &n)| {
        while let Some(&v) = hist.last() {
            if counter[n as usize] < counter[v as usize] {
                break;
            }
            hist.pop();
        }
        if let Some(&v) = hist.last() {
            ans[i] = v;
        }
        hist.push(n);
    });

    let mut stdout = std::io::BufWriter::with_capacity(1 << 18, std::io::stdout());
    for n in ans {
        write!(stdout, "{n} ");
    }
    stdout.write(&[b'\n']);
}
