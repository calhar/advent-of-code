Advent of Code 2024 Day 9
=========================

Not a challenging problem, although needing two distinctly different solutions -- possibly my solution for part 1
could be more efficient though. As far as part two goes, I'm quite happy with it. I think it's about as efficient
as it could be -- though I'm sure someone on the subreddit produced some <1ms monster. Pretty printing this for
debugging would be a bit of a nightmare -- glad I didn't need to deal with that and the one bug I introduced was
a very straightforward oversight.

All in all, quite simple. Still don't think I'm writing idiomatic go properly though -- `moveToEmptyBlock`
probably just wants to be `moveTo`? And `parseData` feels like an absolute mess.