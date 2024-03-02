// https://www.acmicpc.net/problem/1920

#![allow(unused_must_use)]

use std::io::Write;

fn main() {
    let stdin = std::io::stdin();
    let mut stdout = std::io::BufWriter::new(std::io::stdout().lock());

    let mut line = String::new();
    stdin.read_line(&mut line);

    line.clear();
    stdin.read_line(&mut line);
    let numbers: std::collections::HashSet<i32> = std::collections::HashSet::from_iter(
        line.trim_end()
            .split_ascii_whitespace()
            .map(|token| token.parse::<i32>().unwrap()),
    );

    line.clear();
    stdin.read_line(&mut line);
    line.clear();
    stdin.read_line(&mut line);
    line.trim_end()
        .split_ascii_whitespace()
        .map(|token| numbers.contains(&token.parse::<i32>().unwrap()))
        .for_each(|ok| {
            if ok {
                writeln!(stdout, "1");
            } else {
                writeln!(stdout, "0");
            }
        })
}
