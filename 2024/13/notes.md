Advent of Code 2024 day 13
==========================

Just solving linear equations, simple lookup then implementation of 2x2 matrix inverse.
Messed up my `a%det != 0 || b%det != 0` clause and used `&&` instead which led to a rabbit hole of reimplimentation
using `math/big`. Not a fan -- the methods mutating the `Int` that you are calling on seems pretty sketchy and
leads to a bunch of pointless boilerplate if you don't want to be writing `big.NewInt(0).Mul(x, y)` everywhere.

I assume there's a reason for it