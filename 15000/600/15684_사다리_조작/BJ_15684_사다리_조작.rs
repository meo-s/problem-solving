// https://www.acmicpc.net/problem/15684

use std::io::{stdin, Read};

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

type LadderMap = Vec<Vec<bool>>;

fn read_input() -> String {
    let mut buf = Vec::with_capacity(1 << 20);
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn simulate(h: usize, w: usize, has_ladder: &LadderMap) -> bool {
    (0..w).all(|x0| {
        let mut x = x0;
        for y in 0..h {
            if has_ladder[y][x] {
                x += 1;
            } else if 0 < x && has_ladder[y][x - 1] {
                x -= 1;
            }
        }
        x == x0
    })
}

fn brute_force(h: usize, w: usize, ladders: &mut LadderMap, chance: usize, offset: usize) -> bool {
    if chance == 0 {
        simulate(h, w, ladders)
    } else {
        (offset..(h * (w - 1))).any(|yx| {
            let y = yx / (w - 1);
            let x = yx % (w - 1);
            if ladders[y][x] {
                false
            } else {
                ladders[y][x] = true;
                if brute_force(h, w, ladders, chance - 1, yx + 1) {
                    true
                } else {
                    ladders[y][x] = false;
                    false
                }
            }
        })
    }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let (w, m, h) = scan!(inp, _, _, _);
    let mut ladders = vec![vec![false; w]; h];
    for _ in 0..m {
        let (y, x) = scan!(inp, usize, usize);
        ladders[y - 1][x - 1] = true;
    }

    if let Some(ans) = (0..=3).find(|it| brute_force(h, w, &mut ladders, *it, 0)) {
        println!("{ans}");
    } else {
        println!("-1");
    }
}
