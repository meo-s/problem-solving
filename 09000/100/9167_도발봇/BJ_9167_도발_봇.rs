// https://www.acmicpc.net/problem/9167

use std::str;

mod sol {
    use std::io::Read;

    macro_rules! recurse_eval {
        ($name:ident, $counter:expr) => {{
            let counter: &mut Counter = $counter;
            let count = if let Some(v) = counter.get(stringify!($name)) {
                *v
            } else {
                0
            };
            counter.insert(String::from(stringify!($name)), count + 1);
            String::from($name(count).eval(counter))
        }};
    }

    macro_rules! component {
        ($name:ident) => {
            struct $name(usize);
            impl NewWithCount for $name {
                fn new_with_count(count: usize) -> Self {
                    $name(count)
                }
            }
        };
        ($name:ident, [ $( $x:expr ),+; $n:expr ]) => {
            component!($name);
            impl Eval for $name {
                fn eval(&self, _: &mut std::collections::HashMap<String, usize>) -> String {
                    static STR_TABLE: [&'static str; $n] = [$($x),+];
                    String::from(STR_TABLE[self.0 % $n])
                }
            }
        };
        ($name:ident, $f:expr) => {
            component!($name);
            impl Eval for $name {
                fn eval(&self, counter: &mut Counter) -> String {
                    $f(counter, self.0)
                }
            }
        }
    }

    trait NewWithCount {
        fn new_with_count(count: usize) -> Self;
    }

    trait Eval {
        fn eval(&self, counter: &mut std::collections::HashMap<String, usize>) -> String;
    }

    type Counter = std::collections::HashMap<String, usize>;

    fn emphasize<'a>(s: &'a mut String) -> &'a mut String {
        let us = unsafe { s.as_bytes_mut() };
        us[0] = us[0].to_ascii_uppercase();
        s
    }

    component!(Taunt, &mut |c: &mut Counter, cnt| {
        let mut taunt = match cnt % 4 {
            0 => recurse_eval!(Sentence, c) + ".",
            1 => {
                format!(
                    "{} {}.",
                    recurse_eval!(Taunt, c),
                    emphasize(&mut recurse_eval!(Sentence, c))
                )
            }
            2 => recurse_eval!(Noun, c) + "!",
            3 => recurse_eval!(Sentence, c) + ".",
            _ => unreachable!(),
        };
        emphasize(&mut taunt);
        taunt
    });

    component!(Sentence, &mut |c: &mut Counter, cnt| {
        match cnt % 3 {
            0 => format!(
                "{} {}",
                recurse_eval!(PastRel, c),
                recurse_eval!(NounPhrase, c)
            ),
            1 => format!(
                "{} {}",
                recurse_eval!(PresentRel, c),
                recurse_eval!(NounPhrase, c)
            ),
            2 => format!(
                "{} {} {}",
                recurse_eval!(PastRel, c),
                recurse_eval!(Article, c),
                recurse_eval!(Noun, c)
            ),
            _ => unreachable!(),
        }
    });

    component!(NounPhrase, &mut |c: &mut Counter, _| {
        format!(
            "{} {}",
            recurse_eval!(Article, c),
            recurse_eval!(ModifiedNoun, c)
        )
    });

    component!(ModifiedNoun, &mut |c: &mut Counter, cnt| {
        match cnt % 2 {
            0 => recurse_eval!(Noun, c),
            1 => format!("{} {}", recurse_eval!(Modifier, c), recurse_eval!(Noun, c)),
            _ => unreachable!(),
        }
    });

    component!(Modifier, &mut |c: &mut Counter, cnt| {
        match cnt % 2 {
            0 => recurse_eval!(Adjective, c),
            1 => format!(
                "{} {}",
                recurse_eval!(Adverb, c),
                recurse_eval!(Adjective, c)
            ),
            _ => unreachable!(),
        }
    });

    component!(PresentRel, &mut |c: &mut Counter, _| {
        format!(
            "{} {} {}",
            "your",
            recurse_eval!(PresentPerson, c),
            recurse_eval!(PresentVerb, c)
        )
    });

    component!(PastRel, &mut |c: &mut Counter, _| {
        format!(
            "{} {} {}",
            "your",
            recurse_eval!(PastPerson, c),
            recurse_eval!(PastVerb, c)
        )
    });

    component!(PresentPerson, ["steed", "king", "first-born"; 3]);

    component!(PastPerson, ["mother", "father", "grandmother", "grandfather", "godfather"; 5]);

    component!(Noun, ["hamster", "coconut", "duck", "herring", "newt", "peril", "chicken", "vole", "parrot", "mouse", "twit"; 11]);

    component!(PresentVerb, ["is", "masquerades as"; 2]);

    component!(PastVerb, ["was", "personified"; 2]);

    component!(Article, ["a"; 1]);

    component!(Adjective, ["silly", "wicked", "sordid", "naughty", "repulsive", "malodorous", "ill-tempered"; 7]);

    component!(Adverb, ["conspicuously", "categorically", "positively", "cruelly", "incontrovertibly"; 5]);

    fn refine(s: &str) -> (String, usize) {
        let mut res = String::new();
        let mut num_words = 0;
        s.split_ascii_whitespace().for_each(|token| {
            if !token.is_empty() {
                if !res.is_empty() {
                    res.push(' ');
                }
                if token.as_bytes().iter().any(|ch| ch.is_ascii_alphabetic()) {
                    num_words += 1;
                }
                res.push_str(token)
            }
        });
        (res, num_words / 3 + if num_words % 3 != 0 { 1 } else { 0 })
    }

    pub fn run() {
        let mut inp = vec![];
        std::io::stdin()
            .read_to_end(&mut inp)
            .expect("failed to read input");

        let mut ans = String::new();
        let mut counter = Counter::new();
        let taunt_key: String = String::from("Taunt");
        counter.insert(taunt_key.clone(), 0);
        for ref line in unsafe { std::str::from_utf8_unchecked(&inp) }
            .trim_end()
            .split('\n')
        {
            let (line, mut chance) = refine(line);

            let mut matcher = "theholygrail\0".as_bytes().as_ptr();
            for ch in line.as_bytes().iter() {
                if unsafe { *matcher } == b'\0' {
                    break;
                }
                if unsafe { *matcher } == ch.to_ascii_lowercase() {
                    matcher = unsafe { matcher.offset(1) };
                }
            }

            if !ans.is_empty() {
                ans.push('\n');
            }
            ans.push_str(&format!("Knight: {}\n", line));
            if 0 < chance {
                if unsafe { *matcher } == b'\0' {
                    ans.push_str("Taunter: (A childish hand gesture).\n");
                    chance -= 1;
                }
                let prev_taunts = *counter.get(&taunt_key).unwrap();
                while (*counter.get(&taunt_key).unwrap() - prev_taunts) < chance {
                    ans.push_str(&format!(
                        "Taunter: {}\n",
                        recurse_eval!(Taunt, &mut counter)
                    ));
                }
            }
        }

        print!("{ans}");
    }
}

fn main() {
    sol::run();
}
