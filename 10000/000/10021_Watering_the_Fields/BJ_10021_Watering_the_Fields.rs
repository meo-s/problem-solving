// https://www.acmicpc.net/problem/10021

use std::{io::Read, str::FromStr};

fn scan<'a, T: FromStr>(it: &mut impl Iterator<Item = &'a str>) -> T {
    unsafe { str::parse::<T>(it.next().unwrap()).unwrap_unchecked() }
}

fn main() {
    let mut inp = vec![];
    std::io::stdin()
        .read_to_end(&mut inp)
        .expect("failed to read input");

    let mut it = unsafe { std::str::from_utf8_unchecked(&inp) }
        .trim_end()
        .split_whitespace();

    let n = scan::<usize>(&mut it);
    let c = scan::<i32>(&mut it);

    let mut points = vec![];
    for _ in 0..n {
        points.push(((scan::<i32>(&mut it), scan::<i32>(&mut it)), i32::MAX));
    }

    let mut cads = std::collections::BinaryHeap::new();
    let update_cads = |cads: &mut std::collections::BinaryHeap<(i32, usize)>,
                       points: &mut Vec<((i32, i32), i32)>,
                       u: usize| {
        for i in 0..n {
            if i != u && i32::MIN < points[i].1 {
                let dx = points[i].0 .0 - points[u].0 .0;
                let dy = points[i].0 .1 - points[u].0 .1;
                let cost = dx * dx + dy * dy;
                if c <= cost && cost < points[i].1 {
                    points[i].1 = cost;
                    cads.push((-cost, i));
                }
            }
        }
    };

    let mut total_cost = 0_i64;
    let mut num_conns = 0;
    points[0].1 = i32::MIN;
    update_cads(&mut cads, &mut points, 0);
    while num_conns < n - 1 && !cads.is_empty() {
        let (cost, u) = cads.pop().unwrap();
        if points[u].1 != i32::MIN {
            total_cost += -cost as i64;
            num_conns += 1;
            points[u].1 = i32::MIN;
            update_cads(&mut cads, &mut points, u);
        }
    }

    println!("{}", if num_conns == n - 1 { total_cost } else { -1 });
}
