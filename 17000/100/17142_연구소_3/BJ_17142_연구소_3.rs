// https://www.acmicpc.net/problem/17142

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

#[derive(Clone, Copy, PartialEq, Eq)]
enum Cell {
    Empty = 0,
    Wall = 1,
    Virus = 2,
    ActiveVirus = 3,
}

impl From<u8> for Cell {
    fn from(v: u8) -> Self {
        match v {
            b'0' => Cell::Empty,
            b'1' => Cell::Wall,
            b'2' => Cell::Virus,
            _ => unreachable!(),
        }
    }
}

fn read_input() -> String {
    let mut buf = Default::default();
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn simulate(
    n: usize,
    mut remains: usize,
    mut lab: Vec<Vec<Cell>>,
    mut viruses: VecDeque<(usize, usize)>,
) -> u32 {
    let mut secs = 0;
    while 0 < remains && !viruses.is_empty() {
        for _ in 0..viruses.len() {
            let (x, y) = viruses.pop_front().unwrap();
            for (dx, dy) in [(1, 0), (0, usize::MAX), (usize::MAX, 0), (0, 1)] {
                let nx = x.wrapping_add(dx);
                let ny = y.wrapping_add(dy);
                if nx < n && ny < n && (lab[ny][nx] == Cell::Empty || lab[ny][nx] == Cell::Virus) {
                    if lab[ny][nx] == Cell::Empty {
                        remains -= 1;
                    }
                    lab[ny][nx] = Cell::ActiveVirus;
                    viruses.push_back((nx, ny));
                }
            }
        }
        secs += 1;
    }
    if remains == 0 {
        secs
    } else {
        u32::MAX
    }
}

fn brute_force(
    n: usize,
    m: usize,
    remains: usize,
    lab: &Vec<Vec<Cell>>,
    viruses: &[(usize, usize)],
    seed: &mut VecDeque<(usize, usize)>,
) -> u32 {
    if seed.len() == m {
        simulate(
            n,
            remains,
            lab.iter().map(ToOwned::to_owned).collect(),
            seed.clone(),
        )
    } else {
        let mut ans = u32::MAX;
        for i in 0..=(viruses.len() - (m - seed.len())) {
            seed.push_back(viruses[i]);
            ans = ans.min(brute_force(n, m, remains, lab, &viruses[i + 1..], seed));
            seed.pop_back();
        }
        ans
    }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let (n, m) = scan!(inp, _, _);
    let lab = (0..n)
        .map(|_| {
            (0..n)
                .map(|_| Cell::from(inp.next().unwrap().as_bytes()[0]))
                .collect::<Vec<_>>()
        })
        .collect::<Vec<Vec<_>>>();

    let mut viruses = vec![];
    for y in 0..n {
        (0..n)
            .filter(|&x| lab[y][x] == Cell::Virus)
            .for_each(|x| viruses.push((x, y)));
    }

    let remains = lab
        .iter()
        .map(|row| row.iter().filter(|&&it| it == Cell::Empty).count())
        .sum::<usize>();

    let ans = brute_force(n, m, remains, &lab, &viruses, &mut Default::default());
    println!("{}", ans as i32);
}
