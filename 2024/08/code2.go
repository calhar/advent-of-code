package main

import (
	"bufio"
	"fmt"
	"os"
)

type position []int
type antenna struct {
	freq      rune
	positions []position
}
type field [][]rune

func (field *field) contains(position position) bool {
	return position[0] >= 0 && position[0] < len(*field) && position[1] >= 0 && position[1] < len((*field)[position[0]])
}

func (field *field) addAntinode(position position) int {
	if field.contains(position) {
		if (*field)[position[0]][position[1]] != '#' {
			(*field)[position[0]][position[1]] = '#'
			return 1
		}
	}
	return 0
}

func parseData() (field field, antennas map[rune]*antenna) {
	scanner := bufio.NewScanner(os.Stdin)
	i := 0
	antennas = make(map[rune]*antenna)
	for scanner.Scan() {
		text := scanner.Text()
		if text == "" {
			break
		}
		for j, c := range text {
			if c != '.' {
				a, ok := antennas[c]
				if !ok {
					a = &antenna{c, []position{{i, j}}}
					antennas[c] = a
				} else {
					a.positions = append(a.positions, position{i, j})
				}
			}
		}
		field = append(field, []rune(text))
		i++
	}
	return
}

func main() {
	field, antennasByFreq := parseData()

	antinodes := 0

	for _, antennas := range antennasByFreq {
		positions := antennas.positions
		for i, pos := range positions {
			for j := i; j < len(positions); j++ {
				if i == j {
					continue
				}
				diffX := pos[0] - positions[j][0]
				diffY := pos[1] - positions[j][1]

				antinode := position{pos[0], pos[1]}
				for field.contains(antinode) {
					antinodes += field.addAntinode(antinode)
					antinode = position{antinode[0] + diffX, antinode[1] + diffY}
				}
				antinode = position{positions[j][0], positions[j][1]}
				for field.contains(antinode) {
					antinodes += field.addAntinode(antinode)
					antinode = position{antinode[0] - diffX, antinode[1] - diffY}
				}
			}
		}
	}
	fmt.Println(antinodes)
}
