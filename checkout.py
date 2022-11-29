from __future__ import absolute_import

import re
import sys
from argparse import ArgumentParser
from pathlib import Path

import requests
from bs4 import BeautifulSoup

BAEKJOON_ORIGIN = 'https://www.acmicpc.net'


def main(prob_id: int):
    PROB_URI = BAEKJOON_ORIGIN + f'/problem/{prob_id}'

    res = requests.get(PROB_URI)
    if res.status_code != 200:
        if res.status_code == 404:
            print(f'problem(id={prob_id}) is not found')
        return -1

    doc = BeautifulSoup(res.text, 'html.parser')
    if (title_tag := doc.select_one('#problem_title')) is None:
        return -1

    prob_dir = Path(f'{prob_id - (prob_id % 1000):>05}/{100 * (prob_id // 100 % 10):>03}')
    prob_dir = prob_dir.absolute()

    title = re.sub(r'\(|\)|\!|\?', '', title_tag.text.replace(' ', '_'))
    prob_path = prob_dir.joinpath(f'{prob_id}-{title}.py')

    print(f'baekjoon #{prob_id}: {title_tag.text.strip()}')
    print(f'  it will create python source template for baekjoon #{prob_id}')
    print(f'    at {prob_path.as_posix()} ...\n')

    if prob_path.exists():
        print('  this path alrealdy exists.')
        return 0

    print('  enter N/n to stop: ', end='')
    if (input() or 'y').lower()[:1] == 'n':
        return -1

    prob_dir.mkdir(parents=True, exist_ok=True)
    prob_path.touch(exist_ok=True)
    prob_path.write_text(f'# {PROB_URI}\n\nimport sys\n\nreadline = lambda: sys.stdin.readline().strip()\n',
                         encoding='utf-8')


if __name__ == '__main__':
    arg_parser = ArgumentParser()
    add = arg_parser.add_argument
    add('prob_id', type=int)
    sys.exit(main(**vars(arg_parser.parse_args())) or 0)
