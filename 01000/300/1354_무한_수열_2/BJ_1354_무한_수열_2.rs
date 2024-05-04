// https://www.acmicpc.net/problem/1354

use std::{
    collections::HashMap,
    io::{stdin, BufRead},
};

macro_rules! scan {
    ($it:expr, $($ty:ty),+ $(,)?) => {
        {
            let mut it = $it;
            ($(it.next().unwrap().parse::<$ty>().unwrap()),+)
        }
    }
}

fn calc_a(dp: &mut HashMap<i64, i64>, (i, p, q, x, y): (i64, i64, i64, i64, i64)) -> i64 {
    if i <= 0 {
        return 1;
    }
    if let Some(&a_i) = dp.get(&i) {
        a_i
    } else {
        let a_i = calc_a(dp, (i / p - x, p, q, x, y)) + calc_a(dp, (i / q - y, p, q, x, y));
        dp.insert(i, a_i);
        a_i
    }
}

fn main() {
    let mut line = String::new();
    stdin().lock().read_line(&mut line).unwrap();
    println!(
        "{}",
        calc_a(
            &mut HashMap::new(),
            scan!(line.split_ascii_whitespace(), i64, i64, i64, i64, i64)
        )
    );
}
