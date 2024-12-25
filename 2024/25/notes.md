Advent of Code 2024 day 25
==========================

It's Christmas, so that means I get to do what I want, and what I want is to skip ahead and solve todays problem.
Relatively straightforward matching problem, naively `O(N*M)` due to needing to test all candidates against each
other. You can do better by grouping candidates together to eliminate combinations.

Ideally, you'd have one bucket per field, but that's a bit overdone for this -- so bucketing on the first pin
is basically sufficient to eliminate up to 5/6 of the candidates for any given key. Of course a key with a height
of 0 for the first pin unfortunately still needs to be tested against every lock -- due to our grouping and tolerance
criteria.

As far as implementation details go -- I strongly dislike the way I'm parsing the data here. Setting a custom
`SplitFunc` on the `Scanner` is certainly better. Something to add to a utilities module perhaps? I don't think this
is the last I've seen of this particular line break.