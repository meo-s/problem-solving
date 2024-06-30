// https://www.acmicpc.net/problem/3190

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

macro_rules! init {
    ($name:ident) => {
        let mut $name = Default::default();
        stdin().lock().read_to_end(&mut $name).unwrap();
        let $name = unsafe { String::from_utf8_unchecked($name) };
    };
}

const DELTAS: [(usize, usize); 4] = [(0, 1), (usize::MAX, 0), (0, usize::MAX), (1, 0)];

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
enum Cell {
    Empty,
    Snake,
    Apple,
}

fn main() {
    init!(inp);
    let mut inp = inp.split_ascii_whitespace();

    let n = scan!(inp, _);
    let mut board = vec![vec![Cell::Empty; n]; n];
    for _ in 0..scan!(inp, _) {
        let (y, x) = scan!(inp, usize, usize);
        board[y - 1][x - 1] = Cell::Apple;
    }

    let mut ans = 0;
    let inputs = (0..scan!(inp, _)).map(|_| (scan!(inp, usize), inp.next().unwrap()));
    let mut inputs = VecDeque::from_iter(inputs);
    let mut snake = VecDeque::from_iter([(0usize, 0usize)]);
    let mut snake_dir = 0;
    for t in 1.. {
        let (hy, hx) = snake.front().unwrap();
        let (dy, dx) = DELTAS[snake_dir];
        let (ny, nx) = (hy.wrapping_add(dy), hx.wrapping_add(dx));
        if n <= ny || n <= nx || board[ny][nx] == Cell::Snake {
            ans = t;
            break;
        }

        let ate = board[ny][nx] == Cell::Apple;
        board[ny][nx] = Cell::Snake;
        snake.push_front((ny, nx));

        if !ate {
            let (ty, tx) = snake.pop_back().unwrap();
            board[ty][tx] = Cell::Empty;
        }

        if let Some(&(at, key)) = inputs.front() {
            if at == t {
                if key == "L" {
                    snake_dir = (snake_dir + 1) % DELTAS.len();
                } else {
                    snake_dir = (snake_dir + DELTAS.len() - 1) % DELTAS.len();
                }
                inputs.pop_front();
            }
        }
    }

    println!("{ans}");
}
