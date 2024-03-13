// https://www.acmicpc.net/problem/6594

use std::io::Read;
use std::str;

fn eval_num(exp: &[u8], idx: &mut usize) -> i128 {
    let mut v = 0;
    let mut i = *idx;
    loop {
        if i == exp.len() || !exp[i].is_ascii_digit() {
            *idx = i;
            break v;
        }
        v = v * 10 + (exp[i] - b'0') as i128;
        i += 1;
    }
}

fn eval_factor(exp: &[u8], idx: &mut usize) -> (i128, i128) {
    match exp[*idx] {
        b'0'..=b'9' => (0, eval_num(exp, idx)),
        b'x' => {
            *idx += 1;
            (1, 0)
        }
        b'(' => {
            *idx += 1;
            let v = eval_exp(exp, idx);
            *idx += 1;
            v
        }
        _ => unreachable!(),
    }
}

fn eval_term(exp: &[u8], idx: &mut usize) -> (i128, i128) {
    let mut v = eval_factor(exp, idx);
    while *idx < exp.len() && exp[*idx] == b'*' {
        *idx += 1;
        let rhs = eval_factor(exp, idx);
        if v.0 == 0 {
            v.0 = v.1 * rhs.0;
            v.1 = v.1 * rhs.1;
        } else {
            v.0 *= rhs.1;
            v.1 *= rhs.1;
        }
    }
    v
}

fn eval_exp(exp: &[u8], idx: &mut usize) -> (i128, i128) {
    let mut v = eval_term(exp, idx);
    while *idx < exp.len() && (exp[*idx] == b'+' || exp[*idx] == b'-') {
        *idx += 1;
        let sign = if exp[*idx - 1] == b'+' { 1 } else { -1 };
        let rhs = eval_term(exp, idx);
        v.0 += sign * rhs.0;
        v.1 += sign * rhs.1;
    }
    v
}

fn main() {
    let mut inp = vec![];
    std::io::stdin()
        .read_to_end(&mut inp)
        .expect("failed to read input");

    let mut ans = String::new();
    for (test_case, eq) in unsafe { str::from_utf8_unchecked(&inp) }
        .split_whitespace()
        .filter(|line| !line.is_empty())
        .enumerate()
    {
        let mut idx = 0;
        let eq = eq.as_bytes();
        let lhs = eval_exp(eq, &mut idx);
        idx += 1;
        let rhs = eval_exp(eq, &mut idx);

        let x = (lhs.0 - rhs.0) as f64;
        let c = (rhs.1 - lhs.1) as f64;
        if 0 < test_case {
            ans.push('\n');
        }
        ans.push_str(&format!("Equation #{}\n", test_case + 1));
        if x != 0.0 {
            ans.push_str(&format!("x = {:.6}\n", c / x));
        } else if c == 0.0 {
            ans.push_str("Infinitely many solutions.\n");
        } else {
            ans.push_str("No solution.\n");
        }
    }
    print!("{ans}");
}
