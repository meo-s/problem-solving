// https://www.acmicpc.net/problem/9170

use std::io::{BufRead, Write};

fn term(s: &str, idx: usize) -> (i32, usize) {
    match s.chars().nth(idx).unwrap() {
        '(' => {
            let (val, next_idx) = exp(s, idx + 1);
            (val, next_idx + 1)
        }
        '+' => term(s, idx + 1),
        '-' => {
            let (val, next_idx) = term(s, idx + 1);
            (-val, next_idx)
        }
        ch @ _ => (ch.to_digit(10).unwrap() as i32, idx + 1),
    }
}

fn exp(s: &str, idx: usize) -> (i32, usize) {
    let (mut val, mut idx) = term(s, idx);
    loop {
        if s.len() <= idx || !['+', '-'].contains(&s.chars().nth(idx).unwrap()) {
            break (val, idx);
        }
        let res = term(s, idx + 1);
        val = if s.chars().nth(idx).unwrap() == '+' {
            val + res.0
        } else {
            val - res.0
        };
        idx = res.1;
    }
}

fn eval(s: &str) -> i32 {
    exp(&s, 0).0
}

fn main() {
    #![allow(unused_must_use)]

    let mut s = String::new();
    let mut stdin = std::io::BufReader::with_capacity(1 << 20, std::io::stdin());
    let mut stdout = std::io::BufWriter::with_capacity(1 << 18, std::io::stdout());
    while stdin.read_line(&mut s).is_ok() && 0 < s.len() {
        writeln!(stdout, "{}", eval(&s));
        s.clear();
    }
}
