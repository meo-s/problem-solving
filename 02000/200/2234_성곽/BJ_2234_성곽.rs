// https://www.acmicpc.net/problem/2234

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

const DIR_DELTAS: [(usize, usize); 4] = [(0, usize::MAX), (usize::MAX, 0), (0, 1), (1, 0)];

fn size_of(
    castle: &Vec<Vec<u8>>,
    markers: &mut Vec<Vec<usize>>,
    marker: usize,
    y: usize,
    x: usize,
) -> usize {
    markers[y][x] = marker;
    let mut size = 1;
    for (dy, dx) in DIR_DELTAS.iter().enumerate().filter_map(|it| {
        if (castle[y][x] & (1 << it.0)) == 0 {
            Some(*it.1)
        } else {
            None
        }
    }) {
        let ny = y.wrapping_add(dy);
        let nx = x.wrapping_add(dx);
        if ny < castle.len() && nx < castle[0].len() && markers[ny][nx] == usize::MAX {
            size += size_of(castle, markers, marker, ny, nx);
        }
    }
    size
}

fn merged_size_of(
    castle: &Vec<Vec<u8>>,
    markers: &Vec<Vec<usize>>,
    areas: &Vec<usize>,
    y: usize,
    x: usize,
) -> usize {
    DIR_DELTAS
        .iter()
        .enumerate()
        .filter_map(|it| {
            if (castle[y][x] & (1 << it.0)) != 0 {
                Some(*it.1)
            } else {
                None
            }
        })
        .filter_map(|(dy, dx)| {
            let ny = y.wrapping_add(dy);
            let nx = x.wrapping_add(dx);
            if ny < castle.len() && nx < castle[0].len() && markers[ny][nx] != markers[y][x] {
                Some(areas[markers[y][x]] + areas[markers[ny][nx]])
            } else {
                None
            }
        })
        .max()
        .unwrap_or(areas[markers[y][x]])
}

fn main() {
    init!(inp);
    let (w, h) = scan!(inp, _, _);
    let castle: Vec<Vec<_>> = (0..h)
        .map(|_| (0..w).map(|_| scan!(inp, u8)).collect())
        .collect();

    let mut markers = vec![vec![usize::MAX; w]; h];
    let mut areas = vec![];
    for y in 0..h {
        for x in 0..w {
            if markers[y][x] == usize::MAX {
                areas.push(size_of(&castle, &mut markers, areas.len(), y, x));
            }
        }
    }

    let mut ans = String::new();
    ans.push_str(&format!("{}\n", areas.len()));
    ans.push_str(&format!("{}\n", areas.iter().max().unwrap()));
    ans.push_str(&format!(
        "{}\n",
        (0..h)
            .map(|y| (0..w)
                .map(|x| merged_size_of(&castle, &markers, &areas, y, x))
                .max()
                .unwrap())
            .max()
            .unwrap()
    ));
    print!("{ans}");
}
