// https://www.acmicpc.net/problem/16235

use std::{
    collections::VecDeque,
    io::{stdin, Read},
};

macro_rules! parse_next {
    ($it:ident, $ty:ty) => {
        $it.next().unwrap().parse::<$ty>().unwrap()
    };
}

macro_rules! scan {
    ($it:ident, $ty:ty) => {
        parse_next!($it, $ty)
    };
    ($it:ident, $arg0:ty, $($args:ty),+ $(,)?) => {
        (parse_next!($it, $arg0), $(parse_next!($it, $args),)+)
    };
}

const DIRECTIONS: [(usize, usize); 8] = [
    (0, 1),
    (usize::MAX, 1),
    (usize::MAX, 0),
    (usize::MAX, usize::MAX),
    (0, usize::MAX),
    (1, usize::MAX),
    (1, 0),
    (1, 1),
];

fn read_input() -> String {
    let mut buf = Vec::with_capacity(1 << 20);
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let (n, m, k) = scan!(inp, _, _, _);
    let mut nourishments = vec![vec![(5, 0, 0); n]; n];
    for y in 0..n {
        for x in 0..n {
            nourishments[y][x].2 = scan!(inp, _);
        }
    }

    let mut trees = vec![VecDeque::new(); 1011];
    for _ in 0..m {
        let (y, x, z) = scan!(inp, usize, usize, usize);
        trees[z].push_back((0, (y - 1, x - 1)));
    }

    for day in 0..k {
        for age in 1..trees.len() {
            while let Some(&(tag, (y, x))) = trees[age].front() {
                if tag != day {
                    break;
                }

                trees[age].pop_front();
                if nourishments[y][x].0 < age {
                    nourishments[y][x].1 += age / 2;
                } else {
                    nourishments[y][x].0 -= age;
                    trees[age + 1].push_back((tag + 1, (y, x)));
                    if (age + 1) % 5 == 0 {
                        for (dy, dx) in DIRECTIONS {
                            let ny = y.wrapping_add(dy);
                            let nx = x.wrapping_add(dx);
                            if ny < n && nx < n {
                                trees[1].push_back((tag + 1, (ny, nx)));
                            }
                        }
                    }
                }
            }
        }

        for y in 0..n {
            for x in 0..n {
                nourishments[y][x].0 += nourishments[y][x].1 + nourishments[y][x].2;
                nourishments[y][x].1 = 0;
            }
        }
    }
    println!("{}", trees.iter().map(|it| it.len()).sum::<usize>());
}
