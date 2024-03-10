// https://www.acmicpc.net/problem/18809

#![allow(unused_must_use)]

use std::io::Read;
use std::str;

const DIRECTIONS: [(usize, usize); 4] = [(usize::MAX, 0), (0, 1), (1, 0), (0, usize::MAX)];

#[derive(Debug, Clone, Copy)]
enum GardenCell {
    Water,
    Dirt,
    FertileDirt(u8, u16),
    Flower,
}

fn combinations<F: FnMut(u32)>(n: usize, offset: usize, remains: usize, state: u32, f: &mut F) {
    if remains == 0 {
        f(state);
    } else {
        for i in offset..n - remains + 1 {
            if (state & (1 << i)) == 0 {
                combinations(n, i + 1, remains - 1, state | (1 << i), f);
            }
        }
    }
}

fn simulate(
    garden: &mut Vec<Vec<GardenCell>>,
    start_points: &Vec<(usize, usize)>,
    g: u32,
    r: u32,
) -> u16 {
    let n = garden.len();
    let m = garden[0].len();
    for y in 0..n {
        for x in 0..m {
            garden[y][x] = match garden[y][x] {
                GardenCell::FertileDirt(_, _) => GardenCell::Dirt,
                GardenCell::Flower => GardenCell::Dirt,
                v @ _ => v,
            }
        }
    }

    let mut num_flowers = 0_u16;
    let mut pendings = std::collections::VecDeque::<(usize, usize)>::new();
    for i in 0..start_points.len() {
        if (g & (1 << i)) != 0 || (r & (1 << i)) != 0 {
            let class = if (g & (1 << i)) != 0 { b'G' } else { b'R' };
            let (y, x) = start_points[i];
            garden[y][x] = GardenCell::FertileDirt(class, 0);
            pendings.push_back((y, x));
        }
    }
    while let Some((y, x)) = pendings.pop_front() {
        if let GardenCell::FertileDirt(class, when) = garden[y][x] {
            for (dy, dx) in DIRECTIONS {
                let ny = y.wrapping_add(dy);
                let nx = x.wrapping_add(dx);
                if ny == usize::MAX || n <= ny || nx == usize::MAX || m <= nx {
                    continue;
                }
                match garden[ny][nx] {
                    GardenCell::Dirt => {
                        garden[ny][nx] = GardenCell::FertileDirt(class, when + 1);
                        pendings.push_back((ny, nx));
                    }
                    GardenCell::FertileDirt(other_class, other_when) => {
                        if other_class != class && other_when == when + 1 {
                            garden[ny][nx] = GardenCell::Flower;
                            num_flowers += 1;
                        }
                    }
                    _ => {}
                }
            }
        }
    }
    num_flowers
}

fn main() {
    let mut inp = vec![];
    std::io::stdin().read_to_end(&mut inp);

    let mut it = unsafe { str::from_utf8_unchecked(&inp) }.split_whitespace();
    let n = str::parse::<usize>(it.next().unwrap()).unwrap();
    let m = str::parse::<usize>(it.next().unwrap()).unwrap();
    let g = str::parse::<usize>(it.next().unwrap()).unwrap();
    let r = str::parse::<usize>(it.next().unwrap()).unwrap();

    let mut start_points = vec![];
    let mut garden = vec![vec![]; n];
    for y in 0..n {
        for x in 0..m {
            garden[y].push(match unsafe { *it.next().unwrap().as_ptr() } {
                b'2' => {
                    start_points.push((y, x));
                    GardenCell::Dirt
                }
                b'1' => GardenCell::Dirt,
                _ => GardenCell::Water,
            });
        }
    }

    let mut ans = 0;
    combinations(start_points.len(), 0, g, 0, &mut |g| {
        combinations(start_points.len(), 0, r, g, &mut |gr| {
            ans = std::cmp::max(ans, simulate(&mut garden, &start_points, g, gr ^ g));
        });
    });
    println!("{}", ans)
}
