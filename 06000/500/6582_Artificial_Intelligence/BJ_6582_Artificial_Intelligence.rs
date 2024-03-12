// https://www.acmicpc.net/problem/6582

use std::io::Read;
use std::str::{self, FromStr};

fn scan<'a, T: FromStr>(it: &mut impl Iterator<Item = &'a str>) -> T {
    unsafe { str::parse::<T>(it.next().unwrap()).unwrap_unchecked() }
}

fn parse_amount(s: &[u8]) -> f64 {
    let mut i = 0;
    while i < s.len() && (s[i].is_ascii_digit() || s[i] == b'.') {
        i += 1;
    }
    str::parse::<f64>(unsafe { str::from_utf8_unchecked(&s[..i]) }).unwrap()
        * match s[i] {
            b'm' => 1e-3,
            b'k' => 1e+3,
            b'M' => 1e+6,
            _ => 1.0,
        }
}

fn main() {
    let mut inp = vec![];
    std::io::stdin()
        .read_to_end(&mut inp)
        .expect("failed to read input");

    let mut ans = String::new();
    let mut it = unsafe { str::from_utf8_unchecked(&inp) }.split('\n');
    for test_case in 1..=scan::<usize>(&mut it) {
        let s = it.next().unwrap().as_bytes();
        let mut offset = 0;
        let mut amounts = [f64::NAN; 3]; // P, U, I
        while let Some(idx) = s.iter().skip(offset).position(|&v| v == b'=') {
            let concept = match s[offset + idx - 1] {
                b'P' => 0,
                b'U' => 1,
                b'I' => 2,
                _ => unreachable!(),
            };
            amounts[concept] = parse_amount(&s[offset + idx + 1..]);
            offset += idx + 1;
        }

        if 1 < test_case {
            ans.push('\n');
        }
        ans.push_str(&format!("Problem #{}\n", test_case));
        if f64::is_nan(amounts[0]) {
            ans.push_str(&format!("P={:.2}W\n", amounts[1] * amounts[2]));
        } else if f64::is_nan(amounts[1]) {
            ans.push_str(&format!("U={:.2}V\n", amounts[0] / amounts[2]));
        } else {
            ans.push_str(&format!("I={:.2}A\n", amounts[0] / amounts[1]));
        }
    }

    println!("{ans}");
}
