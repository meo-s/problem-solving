// https://www.acmicpc.net/problem/14713

use std::io::BufRead;

fn get_line<'a, Reader: BufRead>(reader: &mut Reader, line: &'a mut String) -> &'a String {
    #![allow(unused_must_use)]
    line.clear();
    reader.read_line(line);
    line.pop();
    line
}

fn main() {
    let mut line = String::new();
    let mut stdin = std::io::BufReader::with_capacity(1 << 20, std::io::stdin());

    let n = str::parse::<usize>(get_line(&mut stdin, &mut line)).unwrap();

    let mut tokens = std::collections::HashMap::new();
    for i in 0..n {
        for (j, token) in get_line(&mut stdin, &mut line).split(' ').enumerate() {
            tokens.insert(String::from(token), (i as u32, j as u32));
        }
    }

    let mut token_count = 0;
    let mut indices = vec![0_u32; n];
    let matches = get_line(&mut stdin, &mut line).split(' ').all(|token| {
        token_count += 1;
        if let Some((i, j)) = tokens.get(token) {
            if indices[*i as usize] == *j {
                indices[*i as usize] += 1;
                return true;
            }
        }
        false
    });
    println!(
        "{}",
        if matches && token_count == tokens.len() {
            "Possible"
        } else {
            "Impossible"
        }
    )
}
