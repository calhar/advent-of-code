Advent of Code 2024 day 8
=========================

Fairly straightforward again, experimenting further with structs and type definitions. Needing to dereference the
`*field` to index the underlying slices makes sense, but it feels slightly unintuitive when field accessors just
work e.g.:

```
type field struct {
    map [][]rune
}

func (field *field) contains(x int, y int) bool {
    return x >= 0 && len(field.map) && y >= 0 && len(field.map[x])
}
```

In this respect, the `->` operator from C feels a little more natural to me. Largely because I'm familiar with the
idea of working with pointers and references, but needing to interact with them in different ways from the base
objects.

As far as the problem goes, I'm not sure what there really is to say about parts 1 and 2. Maybe a slightly more
efficient way is to do the antinode calculation while iterating through the field, instead of iterating once to
populate a map and the positions? I don't think it really matters though