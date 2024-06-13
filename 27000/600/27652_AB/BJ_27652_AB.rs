// https://www.acmicpc.net/problem/27652

use std::io::{stdin, Read};

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

#[derive(Debug, Default)]
struct Trie {
    nodes: [Option<Box<Trie>>; 26],
    count: usize,
}

impl Trie {
    fn insert<'a>(&mut self, mut s: impl Iterator<Item = &'a u8>) {
        let mut next = self;
        while let Some(ch) = s.next() {
            let ch = (*ch - b'a') as usize;
            if next.nodes[ch].is_none() {
                next.nodes[ch] = Some(Box::default());
            }
            next = next.nodes[ch].as_mut().unwrap();
            next.count += 1;
        }
    }

    fn delete<'a>(&mut self, mut s: impl Iterator<Item = &'a u8>) {
        if let Some(ch) = s.next() {
            let ch = (*ch - b'a') as usize;
            if let Some(next) = self.nodes[ch].as_mut() {
                next.count -= 1;
                if 0 < next.count {
                    next.delete(s);
                } else {
                    self.nodes[ch] = None;
                }
            }
        }
    }

    fn find<'a>(&self, mut s: impl Iterator<Item = &'a u8>) -> Option<usize> {
        let mut here = self;
        while let Some(ch) = s.next() {
            let ch = (*ch - b'a') as usize;
            if let Some(next) = here.nodes[ch].as_ref() {
                here = next.as_ref();
            } else {
                return None;
            }
        }
        Some(here.count)
    }

    fn find_all(mut prefixes: &Trie, suffixes: &Trie, s: &[u8]) -> usize {
        let mut count = 0;
        for i in 0..(s.len() - 1) {
            let ch = (s[i] - b'a') as usize;
            if let Some(next) = prefixes.nodes[ch].as_ref() {
                prefixes = next.as_ref();
                if let Some(matched) = suffixes.find(s[i + 1..].iter().rev()) {
                    count += prefixes.count * matched;
                }
            } else {
                break;
            }
        }
        count
    }
}

fn main() {
    init!(inp);
    let mut ans = String::new();
    let (mut a, mut b) = (Trie::default(), Trie::default());
    for _ in 0..scan!(inp, _) {
        match inp.next() {
            Some("add") => {
                if inp.next() == Some("A") {
                    a.insert(inp.next().unwrap().as_bytes().iter());
                } else {
                    b.insert(inp.next().unwrap().as_bytes().iter().rev());
                }
            }
            Some("delete") => {
                if inp.next() == Some("A") {
                    a.delete(&mut inp.next().unwrap().as_bytes().iter());
                } else {
                    b.delete(&mut inp.next().unwrap().as_bytes().iter().rev());
                }
            }
            Some("find") => {
                ans.push_str(&format!(
                    "{}\n",
                    Trie::find_all(&a, &b, inp.next().unwrap().as_bytes())
                ));
            }
            _ => unreachable!(),
        }
    }

    print!("{ans}");
}
