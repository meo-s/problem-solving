// https://www.acmicpc.net/problem/14391

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

macro_rules! init {
    ($name:ident) => {
        let mut $name = Default::default();
        stdin().lock().read_to_end(&mut $name).unwrap();
        let $name = unsafe { String::from_utf8_unchecked($name) };
        let mut $name = $name.split_ascii_whitespace();
    };
}

fn brute_force(paper: &Vec<Vec<u32>>, visited: &mut Vec<Vec<bool>>, offset: usize) -> u32 {
    let (h, w) = (paper.len(), paper[0].len());
    let y = offset / w;
    let x = offset % w;
    if y == h {
        return 0;
    }

    if visited[y][x] {
        brute_force(paper, visited, offset + 1)
    } else {
        visited[y][x] = true;
        let mut max_sum = paper[y][x] + brute_force(paper, visited, offset + 1);

        let mut ny = y + 1;
        let mut n = paper[y][x];
        while ny < h && !visited[ny][x] {
            visited[ny][x] = true;
            n = n * 10 + paper[ny][x];
            max_sum = max_sum.max(n + brute_force(paper, visited, offset + 1));
            ny += 1;
        }
        for py in (y + 1)..ny {
            visited[py][x] = false;
        }

        let mut nx = x + 1;
        let mut n = paper[y][x];
        while nx < w && !visited[y][nx] {
            visited[y][nx] = true;
            n = n * 10 + paper[y][nx];
            max_sum = max_sum.max(n + brute_force(paper, visited, offset + 1));
            nx += 1;
        }
        for px in (x + 1)..nx {
            visited[y][px] = false;
        }

        visited[y][x] = false;
        max_sum
    }
}

fn main() {
    init!(inp);
    let (h, w) = scan!(inp, _, _);
    let paper: Vec<Vec<_>> = (0..h)
        .map(|_| {
            inp.next()
                .unwrap()
                .as_bytes()
                .iter()
                .map(|v| (v - b'0') as u32)
                .collect()
        })
        .collect();

    let ans = brute_force(&paper, &mut vec![vec![false; w]; h], 0);
    println!("{ans}");
}
