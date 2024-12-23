package main

import (
	"bufio"
	"fmt"
	"os"
)

var chdir map[rune]rune = map[rune]rune{
	'^': '>',
	'>': 'v',
	'v': '<',
	'<': '^',
}

type guard struct {
	x   int
	y   int
	dir rune
}

type field []string

func (f field) contains(x int, y int) bool {
	return (x >= 0 && x < len(f)) && (y >= 0 && y < len(f[x]))
}

func (self guard) walk(field field) int {
	covered := 1
	for {
		oldX := self.x
		oldY := self.y
		switch self.dir {
		case '^':
			self.x--
		case '>':
			self.y++
		case '<':
			self.y--
		case 'v':
			self.x++
		}

		if field.contains(self.x, self.y) {
			switch field[self.x][self.y] {
			case '#':
				self.x = oldX
				self.y = oldY
				self.dir = chdir[self.dir]
			case '.':
				field[self.x] = field[self.x][:self.y] + "X" + field[self.x][self.y+1:]
				covered++
			case '^', 'X':
				continue
			}
		} else {
			break
		}
	}
	return covered
}

func parseData() (field, guard) {
	var field field
	var g guard
	scanner := bufio.NewScanner(os.Stdin)
	i := 0
	for scanner.Scan() {
		text := scanner.Text()
		if text == "" {
			break
		}
		for j, c := range text {
			if c == '^' {
				g = guard{i, j, c}
			}
		}
		field = append(field, text)
		i++
	}
	return field, g
}

func main() {
	field, guard := parseData()
	covered := guard.walk(field)

	fmt.Println(covered)
}
