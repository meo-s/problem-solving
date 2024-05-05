// https://www.acmicpc.net/problem/12906

use std::{
    collections::VecDeque,
    io::{stdin, Read},
};

fn encode_from_str(mut state: u32, offset: usize, v: &str) -> (u32, usize) {
    let mut pos = offset;
    for ch in v.bytes() {
        state |= match ch {
            b'A' => 0b01,
            b'B' => 0b10,
            b'C' => 0b11,
            _ => unreachable!(),
        } << (pos * 2);
        pos += 1;
    }
    (state, pos)
}

fn encode(state: &[(u32, usize); 3]) -> u32 {
    state[0].0
        | (state[1].0 << ((state[0].1 + 1) << 1))
        | (state[2].0 << ((state[1].1 + state[0].1 + 2) << 1))
}

fn decode(state: u32) -> [(u32, usize); 3] {
    let mut dec_state = [(0, 0); 3];
    let mut pos = 0;
    for i in 0..3 {
        while ((state >> (pos << 1)) & 0b11) != 0 {
            let v = (state >> (pos << 1)) & 0b11;
            dec_state[i].0 |= v << (dec_state[i].1 << 1);
            dec_state[i].1 += 1;
            pos += 1;
        }
        pos += 1;
    }
    dec_state
}

fn read_input() -> u32 {
    macro_rules! next_tower {
        ($it:expr) => {{
            if $it.next().unwrap() == "0" {
                ""
            } else {
                $it.next().unwrap()
            }
        }};
    }

    let mut buf = vec![];
    stdin().lock().read_to_end(&mut buf).unwrap();
    let inp = String::from_utf8_lossy(&buf);

    let mut it = inp.split_ascii_whitespace();
    let mut state = encode_from_str(0, 0, next_tower!(it));
    state = encode_from_str(state.0, state.1 + 1, next_tower!(it));
    encode_from_str(state.0, state.1 + 1, next_tower!(it)).0
}

fn is_repeat_of(state: u32, bits: u32) -> bool {
    for i in 0..16 {
        let v = (state >> (i << 1)) & 0b11;
        if v == 0 {
            break;
        }
        if v != bits {
            return false;
        }
    }
    true
}

fn push(tower: &mut (u32, usize), v: u32) {
    if (0b01..=0b11).contains(&v) {
        tower.0 |= v << (tower.1 << 1);
        tower.1 += 1;
    }
}

fn pop(tower: &mut (u32, usize)) -> u32 {
    if tower.1 == 0 {
        0
    } else {
        tower.1 -= 1;
        let v = (tower.0 >> (tower.1 << 1)) & 0b11;
        tower.0 &= (1 << (tower.1 << 1)) - 1;
        v
    }
}

fn main() {
    let initial_state = read_input();

    let mut hist = vec![usize::MAX; 1 << 24];
    hist[initial_state as usize] = 0;

    let mut pending = VecDeque::from_iter([initial_state]);
    while let Some(enc_state) = pending.pop_front() {
        let state = decode(enc_state);

        if (0..3).all(|it| is_repeat_of(state[it].0, (it + 1) as u32)) {
            println!("{}", hist[enc_state as usize]);
            return;
        }

        for i in 0..3 {
            if state[i].1 == 0 || is_repeat_of(state[i].0, (i + 1) as u32) {
                continue;
            }
            for j in (0..3).filter(|&it| it != i) {
                let mut state = state.clone();
                let plate = pop(&mut state[i]);
                push(&mut state[j], plate);

                let next_state = encode(&state);
                if hist[next_state as usize] == usize::MAX {
                    hist[next_state as usize] = hist[enc_state as usize] + 1;
                    pending.push_back(next_state);
                }
            }
        }
    }
}
