Advent of Code 2024 Day 5
=========================

I have just learned that golang does not actually have sets... this is definitely a choice? Yes, you can
mimic them via `map[int]bool` or `map[int]struct{}`, and there are open source modules to implement them.
This seems absolutely _wild_ to me though, sets are an enormous part of computer science and programming
and to not have them as a built in like arrays or maps seems like a very strange choice to me.

Anyway, todays problem seems relatively straightforward to me, although I'm not happy with the verbosity.
Basically, we're constructing maps of required orderings, then for step 1, if we encounter an update value
that has some required order we've already encountered -- we know the update is bad.  
Then for step 2, we invert this map and use it to construct a valid update given a set of updating values.
The problem has been relatively kind to us and not required any backtracking or similar as part of this,
there are certain scenarios where two or more updates could have been a valid choice at one point, which
would then break later.