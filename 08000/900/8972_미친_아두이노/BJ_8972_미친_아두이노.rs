// https://www.acmicpc.net/problem/8972

use std::{
    collections::VecDeque,
    io::{stdin, Read},
    ops::Add,
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

macro_rules! init {
    ($name:ident) => {
        let mut $name = Default::default();
        stdin().lock().read_to_end(&mut $name).unwrap();
        let $name = unsafe { String::from_utf8_unchecked($name) };
        let mut $name = $name.split_ascii_whitespace();
    };
}

#[derive(Debug, Default, Clone, Copy, PartialEq, Eq)]
struct Vec2 {
    x: usize,
    y: usize,
}

impl From<(usize, usize)> for Vec2 {
    fn from((x, y): (usize, usize)) -> Self {
        Self { x, y }
    }
}

impl Add<Vec2> for Vec2 {
    type Output = Self;
    fn add(self, rhs: Vec2) -> Self::Output {
        Self {
            x: self.x.wrapping_add(rhs.x),
            y: self.y.wrapping_add(rhs.y),
        }
    }
}

fn toward(from: usize, to: usize) -> usize {
    if from < to {
        1
    } else if to < from {
        usize::MAX
    } else {
        0
    }
}

fn main() {
    init!(inp);

    let (h, w) = scan!(inp, usize, usize);
    let mut board = vec![vec![0usize; w]; h];
    let mut arduino = Vec2::default();
    let mut waiting = VecDeque::new();
    for y in 0..h {
        for (x, &ch) in inp.next().unwrap().as_bytes().iter().enumerate() {
            match ch {
                b'R' => {
                    board[y][x] = 1;
                    waiting.push_back(Vec2 { x, y });
                }
                b'I' => {
                    arduino.x = x;
                    arduino.y = y;
                }
                _ => (),
            }
        }
    }

    let dirs = vec![
        Vec2::from((usize::MAX, 1)),
        Vec2::from((0, 1)),
        Vec2::from((1, 1)),
        Vec2::from((usize::MAX, 0)),
        Vec2::from((0, 0)),
        Vec2::from((1, 0)),
        Vec2::from((usize::MAX, usize::MAX)),
        Vec2::from((0, usize::MAX)),
        Vec2::from((1, usize::MAX)),
    ];
    for (turn, &ch) in inp.next().unwrap().as_bytes().iter().enumerate() {
        arduino = arduino + dirs[(ch - b'1') as usize];
        if board[arduino.y][arduino.x] != 0 {
            println!("kraj {}", turn + 1);
            return;
        }

        for _ in 0..waiting.len() {
            let pt = waiting.pop_front().unwrap();
            board[pt.y][pt.x] -= 1;

            let pt = Vec2 {
                x: pt.x.wrapping_add(toward(pt.x, arduino.x)),
                y: pt.y.wrapping_add(toward(pt.y, arduino.y)),
            };
            if pt == arduino {
                println!("kraj {}", turn + 1);
                return;
            }

            board[pt.y][pt.x] += 1;
            waiting.push_back(pt);
        }

        for _ in 0..waiting.len() {
            let pt = waiting.pop_front().unwrap();
            if board[pt.y][pt.x] == 1 {
                waiting.push_back(pt);
            } else {
                board[pt.y][pt.x] = 0;
            }
        }
    }

    let mut ans = String::new();
    for y in 0..h {
        for x in 0..w {
            if arduino == Vec2::from((x, y)) {
                ans.push('I');
            } else if 0 < board[y][x] {
                ans.push('R');
            } else {
                ans.push('.');
            }
        }
        ans.push('\n');
    }

    print!("{ans}")
}
