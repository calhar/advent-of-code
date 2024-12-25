package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

var mutations map[int][]int = make(map[int][]int)

func parseData() []int {
	scanner := bufio.NewScanner(os.Stdin)
	var stones []int
	for scanner.Scan() {
		text := scanner.Text()
		if text == "" {
			break
		}

		tokens := strings.Split(text, " ")
		stones = make([]int, len(tokens))
		for i, token := range tokens {
			stones[i], _ = strconv.Atoi(token)
		}
	}
	return stones
}

func stoneLength(stone int) int {
	if stone == 0 {
		return 1
	}
	count := 0
	for stone != 0 {
		stone /= 10
		count++
	}
	return count
}

func pow10(x int) int {
	if x == 0 {
		return 1
	}
	if x == 1 {
		return 10
	}
	result := 10
	for i := 2; i <= x; i++ {
		result *= 10
	}
	return result
}

func split(stone int, length int) (int, int) {
	half := length / 2
	power10 := pow10(half)

	return stone / power10, stone % power10
}

func mutateStone(stone int) (stone1 int, stone2 int) {
	stone2 = -1
	if stone == 0 {
		stone1 = 1
	} else if length := stoneLength(stone); length%2 == 0 {
		stone1, stone2 = split(stone, length)
	} else {
		stone1 = stone * 2024
	}
	return
}

func mutate(stones []int, generations int) int {
	current := make(map[int]int)
	for _, stone := range stones {
		current[stone]++
	}

	for i := 0; i < generations; i++ {
		next := make(map[int]int)
		for stone, count := range current {
			stone1, stone2 := mutateStone(stone)
			next[stone1] += count
			if stone2 != -1 {
				next[stone2] += count
			}
		}
		current = next
	}

	count := 0
	for _, num := range current {
		count += num
	}
	return count
}

func main() {
	stones := parseData()

	fmt.Println("Part 1: ", mutate(stones, 25))
	fmt.Println("Part 2: ", mutate(stones, 75))
}
