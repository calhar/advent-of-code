Advent of Code 2024 day 10
==========================

Another dfs problem, part 1 needs you to only count unique peaks which requires allows you to avoid a lot of
unnecessary work by keeping track of where you have visited. Part 2 removes this restriction

Fairly straightforward, though I'm not happy with my solution in either case. I've also corrected a prior
misunderstanding around slices and how they are passed. Because I'm apparently very stupid, my reading of "everything
is passed by value, unless its passed by reference" was that passing a slice by value will create a copy of the data
structure. And not the far more sensible reality where slices are actually a `SliceHeader` under the hood, which is
really just a pointer to the actual data structure.  
So passing this by value does create a copy of the `SliceHeader`, but that's perfectly fine