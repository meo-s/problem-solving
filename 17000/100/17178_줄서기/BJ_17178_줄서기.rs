// https://www.acmicpc.net/problem/17178

use std::{cmp::Reverse, io::Read};

type Ticket = (char, usize);

fn read_inp() -> Vec<Ticket> {
    let mut inp = Vec::new();
    std::io::stdin().lock().read_to_end(&mut inp).unwrap();
    String::from_utf8_lossy(&inp)
        .split_ascii_whitespace()
        .skip(1)
        .map(|it| {
            let tokens = it.split("-").collect::<Vec<&str>>();
            (
                tokens[0].chars().nth(0).unwrap(),
                tokens[1].parse::<usize>().unwrap(),
            )
        })
        .collect()
}

fn update_waitings(waitings: &mut Vec<Ticket>, turn: &mut Vec<Ticket>) {
    while !waitings.is_empty() && waitings.last() == turn.last() {
        waitings.pop();
        turn.pop();
    }
}

fn main() {
    let mut tickets = read_inp();
    let mut turn = tickets.clone();
    turn.sort_unstable_by_key(|v| Reverse(*v));
    let mut waitings = Vec::new();
    tickets.reverse();
    loop {
        update_waitings(&mut waitings, &mut turn);
        if let Some(ticket) = tickets.pop() {
            if ticket == *turn.last().unwrap() {
                turn.pop();
            } else {
                waitings.push(ticket);
            }
        } else {
            break;
        }
    }
    if turn.is_empty() {
        println!("GOOD");
    } else {
        println!("BAD");
    }
}
