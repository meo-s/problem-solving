// https://www.acmicpc.net/problem/15685

use std::io::Read;

const DIRECTIONS: [(usize, usize); 4] = [(0, 1), (usize::MAX, 0), (0, usize::MAX), (1, 0)];
type Board = [[bool; 101]; 101];

fn update_board(board: &mut Board, y: usize, x: usize, d: usize) -> (usize, usize) {
    let ny = y.wrapping_add(DIRECTIONS[d].0);
    let nx = x.wrapping_add(DIRECTIONS[d].1);
    board[y][x] = true;
    board[ny][nx] = true;
    (ny, nx)
}

fn is_rectangle(board: &Board, y: usize, x: usize) -> bool {
    board[y][x] && board[y][x + 1] && board[y + 1][x + 1] && board[y + 1][x]
}

fn main() {
    macro_rules! parse_next {
        ($it:expr) => {
            $it.next().unwrap().parse().unwrap()
        };
    }

    let mut inp = Vec::new();
    std::io::stdin().read_to_end(&mut inp).unwrap();

    let inp = String::from_utf8_lossy(&inp);
    let mut it = inp.split_ascii_whitespace();
    let mut board = [[false; 101]; 101];
    let mut path = vec![];
    for _ in 0..parse_next!(it) {
        let mut x = parse_next!(it);
        let mut y = parse_next!(it);
        let dir = parse_next!(it);
        path.clear();
        path.push(dir);
        (y, x) = update_board(&mut board, y, x, dir);
        for _ in 1..=parse_next!(it) {
            for i in (0..path.len()).rev() {
                let dir = (path[i] + 1) % 4;
                (y, x) = update_board(&mut board, y, x, dir);
                path.push(dir);
            }
        }
    }

    let ans = (0..(board.len() - 1))
        .map(|y| {
            (0..(board.len() - 1))
                .filter(|&x| is_rectangle(&board, y, x))
                .count()
        })
        .sum::<usize>();
    println!("{ans}");
}
