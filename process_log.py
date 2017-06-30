#! /usr/bin/python3

import json
import csv
from collections import defaultdict

def card_to_t(card):
    """Convert a java card dict to a touple"""
    return (card['colour'], card['value'])

def unique_cards(cards):
    """Convert a list of card touples to a set"""
    return set(cards)

def intersection(l1, l2):
    """Find items common to both list, taking into account duplicates
    
    There has got to be a better way of doing this -,-
    """

    # if l2 is bigger, flip the arguments
    if (len(l1) > len(l2)):
        return intersection(l2, l1)

    tmp = list(l1)
    common = []

    for item in l2:
        if item not in tmp:
            continue
        else:
            common.append(item)
            tmp.remove(item)
    return common


def get_overlaps(slot, possible):
    my_cards = possible[slot]
    total = 0

    for (other_slot, cards) in possible.items():
        if other_slot == slot:
            continue
        total += len(intersection(my_cards, cards))
    return total

# [ [(R,1), (R,2)], [(R,2), (R,1)] ]
def calc_universes(possible):
    """no duplicates, choose 1 element from each pool, although i'm not sure if it will work"""

    # if we're the last element, there are n possible combinations
    if len(possible) == 1:
        return len(possible[0])

    totals = 0
    head = possible[0]
    tail = possible[1:]
    for item in head:
        # clone tail so we can remove stuff from it
        tail_c = []
        # if we pick an item, it's not available for any other selections
        for tail_x in tail:
            tail_x_c = list(tail_x)
            if item in tail_x_c:
                tail_x_c.remove(item)
            tail_c.append(tail_x_c)
        totals += calc_universes(tail_c)

    return totals

def calc_universes_upper(possible):
    """This is the calcuation we were doing in calc, it will massively overestimate."""
    acc = 1
    for x in possible:
        acc *= len(x)
    return acc

csv_records = []
with open("is-debug.json", "rb") as f:
    logs = json.load(f)

    # go though the record is each game
    for record in logs:

        # for each game, get the stats turn by turn
        for stat in record['stats']:
            possible_cards = {}
            overlaps = {}

            # convert cards from dicts to touples
            for (slot,cards) in stat['possibleCards'].items():
                possible_cards[slot] = [card_to_t(c) for c in cards]

            # find out how many cards could be present in more than one slot
            #for (slot,cards) in possible_cards.items():
            #    overlaps[slot] = get_overlaps(slot, possible_cards)

            # estimate the number of possible worlds
            possible_worlds = calc_universes(list(possible_cards.values())) 
    
            # dump this into a dict to see if it's a good predictor of score
            csv_record = {
                "seed": record['seed'],
                "paired": record['agentPaired'],
                "worlds": possible_worlds,
                "moves": record['result']['moves'],
                "score": record['result']['score']
            }
            csv_records.append(csv_record)

with open("is-worlds.csv", 'w') as f:
    w = csv.DictWriter(f, fieldnames=["seed", "paired", "worlds", "moves", "score"])
    w.writeheader()
    w.writerows(csv_records)
