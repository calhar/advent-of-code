package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

type equation struct {
	total      int
	components []int
}

func validMult(z int, y int) bool {
	return z%y == 0
}

func validAdd(z int, y int) bool {
	return z > y
}

func power10(x int) int {
	i := 10
	for i <= x {
		i *= 10
	}
	return i
}

func dfs(current int, total int, numbers []int) bool {
	if len(numbers) == 0 {
		return current == total
	}
	return dfs(current+numbers[0], total, numbers[1:]) || dfs(current*numbers[0], total, numbers[1:]) || dfs(current*power10(numbers[0])+numbers[0], total, numbers[1:])
}

func parseData() []equation {
	scanner := bufio.NewScanner(os.Stdin)
	var out []equation
	for scanner.Scan() {
		text := scanner.Text()
		if text == "" {
			break
		}
		parts := strings.Split(text, ": ")
		total, _ := strconv.Atoi(parts[0])
		var numbers []int
		for _, raw := range strings.Split(parts[1], " ") {
			v, _ := strconv.Atoi(raw)
			numbers = append(numbers, v)
		}
		eq := equation{total, numbers}
		out = append(out, eq)
	}
	return out
}

func main() {
	equations := parseData()

	total := 0
	for _, equation := range equations {
		if dfs(equation.components[0], equation.total, equation.components[1:]) {
			total += equation.total
		}
	}

	fmt.Println(total)
}
