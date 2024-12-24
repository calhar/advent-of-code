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

func dfs(total int, numbers []int) bool {
	n := len(numbers)
	if len(numbers) == 1 {
		return numbers[0] == total
	}
	if validMult(total, numbers[n-1]) && dfs(total/numbers[n-1], numbers[:n-1]) {
		return true
	}
	if validAdd(total, numbers[n-1]) && dfs(total-numbers[n-1], numbers[:n-1]) {
		return true
	}
	return false
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
		if dfs(equation.total, equation.components) {
			total += equation.total
		}
	}

	fmt.Println(total)
}
