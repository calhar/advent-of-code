Advent of Code 2024 day 11
==========================

The traditional problem that explodes in complexity. My first approach here worked, but was dumb and still too much
of a bruteforce implementation. Thought memoising the tranforms would be enough, but it wasn't -- the number of
stones that you're dealing with quickly explodes.
Comparably, just keeping track of the number of each stone and transforming those with each generation does far
better.