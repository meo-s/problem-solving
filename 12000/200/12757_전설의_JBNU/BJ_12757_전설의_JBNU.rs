// https://www.acmicpc.net/problem/12757

use std::{
    collections::BTreeMap,
    io::{stdin, Read},
    ops::Bound,
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

fn read_input() -> String {
    let mut buf = Vec::with_capacity(1 << 20);
    stdin().lock().read_to_end(&mut buf).unwrap();
    unsafe { String::from_utf8_unchecked(buf) }
}

fn search(db: &BTreeMap<i32, i32>, k: i32, key: i32) -> Result<Option<(i32, i32)>, ()> {
    let mut prev = None;

    if let Some((&key_, &v)) = db
        .range((Bound::Unbounded, Bound::Included(key)))
        .rev()
        .take_while(|it| key - it.0 <= k)
        .next()
    {
        prev = (key_, v).into();
    }

    if let Some((&key_, &v)) = db
        .range((Bound::Included(key), Bound::Unbounded))
        .take_while(|it| it.0 - key <= k)
        .next()
    {
        if let Some(prev) = prev {
            if key - prev.0 < key_ - key || prev.0 == key {
                Ok(prev.into())
            } else if key - prev.0 > key_ - key {
                Ok((key_, v).into())
            } else {
                Err(())
            }
        } else {
            Ok((key_, v).into())
        }
    } else {
        Ok(prev)
    }
}

fn main() {
    let inp = read_input();
    let mut inp = inp.split_ascii_whitespace();

    let (n, m, k) = scan!(inp, _, _, _);
    let mut db = BTreeMap::new();
    for _ in 0..n {
        let (k, v) = scan!(inp, i32, i32);
        db.insert(k, v);
    }

    let mut ans = String::new();
    for _ in 0..m {
        match scan!(inp, u8) {
            1 => {
                let (key, v) = scan!(inp, _, _);
                db.insert(key, v);
            }
            2 => {
                let (key, v) = scan!(inp, _, _);
                if let Ok(Some((key_, _))) = search(&db, k, key) {
                    db.insert(key_, v);
                }
            }
            3 => match search(&db, k, scan!(inp, _)) {
                Ok(Some((_, v))) => ans.push_str(&format!("{v}\n")),
                Ok(None) => ans.push_str("-1\n"),
                Err(..) => ans.push_str("?\n"),
            },
            _ => unreachable!(),
        }
    }

    print!("{ans}");
}
