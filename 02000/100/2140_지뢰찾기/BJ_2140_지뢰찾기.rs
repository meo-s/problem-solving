// https://www.acmicpc.ent/problem/2140

#![allow(unused_must_use)]

mod sol {
    use std::io::Read;
    use std::str;

    #[derive(Debug, Clone, Copy)]
    enum Cell {
        BlackBox,
        Digit(u32, u32, bool),
        SafeZone,
        Mine,
    }

    struct Vicinity(usize, (usize, usize), usize);

    impl Vicinity {
        fn new(n: usize, center: (usize, usize)) -> Self {
            Vicinity(n, center, 0)
        }
    }

    impl Iterator for Vicinity {
        type Item = (usize, usize);

        fn next(&mut self) -> Option<Self::Item> {
            const DIRECTIONS: &[(usize, usize)] = &[
                (usize::MAX, 0),
                (usize::MAX, 1),
                (0, 1),
                (1, 1),
                (1, 0),
                (1, usize::MAX),
                (0, usize::MAX),
                (usize::MAX, usize::MAX),
            ];
            loop {
                if self.2 == DIRECTIONS.len() {
                    break None;
                }
                let ny = self.1 .0.wrapping_add(DIRECTIONS[self.2].0);
                let nx = self.1 .1.wrapping_add(DIRECTIONS[self.2].1);
                self.2 = self.2.wrapping_add(1);
                if ny < self.0 && nx < self.0 {
                    break Some((ny, nx));
                }
            }
        }
    }

    fn set_as_mine(
        map: &mut Vec<Vec<Cell>>,
        (y, x): (usize, usize),
        nexts: &mut std::collections::VecDeque<(usize, usize)>,
    ) {
        map[y][x] = Cell::Mine;
        Vicinity::new(map.len(), (y, x)).for_each(|(ny, nx)| {
            if let Cell::Digit(ref mut num_mines, ref mut num_black_boxes, ref mut solved) =
                map[ny][nx]
            {
                *num_mines -= 1;
                *num_black_boxes -= 1;
                if !*solved && (*num_mines == 0 || *num_mines == *num_black_boxes) {
                    *solved = true;
                    nexts.push_back((ny, nx));
                }
            }
        });
    }

    fn set_as_safe(
        map: &mut Vec<Vec<Cell>>,
        (y, x): (usize, usize),
        nexts: &mut std::collections::VecDeque<(usize, usize)>,
    ) {
        map[y][x] = Cell::SafeZone;
        Vicinity::new(map.len(), (y, x)).for_each(|(ny, nx)| {
            if let Cell::Digit(num_mines, ref mut num_black_boxes, ref mut solved) = map[ny][nx] {
                *num_black_boxes -= 1;
                if !*solved && *num_black_boxes == num_mines {
                    *solved = true;
                    nexts.push_back((ny, nx));
                }
            }
        });
    }

    pub fn run() {
        let mut inp = vec![];
        std::io::stdin().read_to_end(&mut inp);
        let mut lines = unsafe { str::from_utf8_unchecked(&inp) }.split_whitespace();

        let n = str::parse::<usize>(lines.next().unwrap()).unwrap();
        let mut map = vec![vec![Cell::BlackBox; n]; n];
        let mut nexts = std::collections::VecDeque::new();

        lines.enumerate().for_each(|(y, line)| {
            line.as_bytes().iter().enumerate().for_each(|(x, ch)| {
                if *ch != b'#' {
                    let num_mines = (ch - b'0') as u32;
                    let num_black_boxes = Vicinity::new(n, (y, x))
                        .filter(|(y, x)| (0 < *y && *y < n - 1 && 0 < *x && *x < n - 1))
                        .count() as u32;
                    map[y][x] = Cell::Digit(
                        num_mines,
                        num_black_boxes,
                        num_mines == 0 || num_mines == num_black_boxes,
                    );
                    if num_mines == 0 || num_mines == num_black_boxes {
                        nexts.push_back((y, x));
                    }
                }
            });
        });

        while let Some((y, x)) = nexts.pop_front() {
            if let Cell::Digit(num_mines, _, _) = map[y][x] {
                Vicinity::new(n, (y, x)).for_each(|(ny, nx)| {
                    if let Cell::BlackBox = map[ny][nx] {
                        if num_mines == 0 {
                            set_as_safe(&mut map, (ny, nx), &mut nexts);
                        } else {
                            set_as_mine(&mut map, (ny, nx), &mut nexts);
                        }
                    }
                });
            }
        }

        println!(
            "{}",
            (0..n)
                .map(|y| {
                    (0..n)
                        .map(|x| match map[y][x] {
                            Cell::Mine => 1,
                            Cell::BlackBox => 1,
                            _ => 0,
                        })
                        .sum::<i32>()
                })
                .sum::<i32>()
        );
    }
}

fn main() {
    sol::run();
}
