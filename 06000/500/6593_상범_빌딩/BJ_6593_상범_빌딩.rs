// https://www.acmicpc.net/problem/6593

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

fn read_input() -> String {
    let mut buf = Default::default();
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn sol<'a>(inp: &mut impl Iterator<Item = &'a str>) -> Result<Option<u32>, ()> {
    let (h, r, c) = scan!(inp, _, _, usize);
    if (h, r, c) == (0, 0, 0) {
        return Err(());
    }

    let mut s = None;
    let mut building: Vec<Vec<_>> = (0usize..h)
        .map(|z| {
            (0usize..r)
                .map(|y| {
                    let row: Vec<_> = inp.next().unwrap().bytes().collect();
                    if s.is_none() {
                        if let Some((x, _)) = row.iter().enumerate().find(|it| *(it.1) == b'S') {
                            s = (z, y, x).into();
                        }
                    }
                    row
                })
                .collect()
        })
        .collect();

    let s = s.unwrap();
    building[s.0][s.1][s.2] = b'#';
    let mut waypoints = VecDeque::from([(1, s)]);
    while let Some((turn, (z, y, x))) = waypoints.pop_front() {
        for (dz, dy, dx) in [
            (usize::MAX, 0, 0),
            (0, usize::MAX, 0),
            (0, 0, usize::MAX),
            (0, 1, 0),
            (0, 0, 1),
            (1, 0, 0),
        ] {
            let nz = z.wrapping_add(dz);
            let ny = y.wrapping_add(dy);
            let nx = x.wrapping_add(dx);
            if h <= nz || r <= ny || c <= nx {
                continue;
            }

            match building[nz][ny][nx] {
                b'.' => {
                    building[nz][ny][nx] = b'#';
                    waypoints.push_back((turn + 1, (nz, ny, nx)));
                }
                b'E' => {
                    return Ok(Some(turn));
                }
                _ => (),
            }
        }
    }

    Ok(None)
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();
    let mut ans = String::new();
    loop {
        match sol(&mut inp) {
            Ok(Some(turn)) => ans.push_str(&format!("Escaped in {turn} minute(s).\n")),
            Ok(None) => ans.push_str("Trapped!\n"),
            Err(..) => {
                print!("{ans}");
                return;
            }
        }
    }
}
