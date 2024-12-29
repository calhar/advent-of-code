package main

import (
	"bufio"
	"fmt"
	"os"
)

type equation struct {
	matrix []int
	prizes []int
}

func (eq equation) det() int {
	return eq.matrix[0]*eq.matrix[3] - eq.matrix[1]*eq.matrix[2]
}

func (eq equation) solve() (int, int) {
	px := eq.prizes[0]
	py := eq.prizes[1]
	det := eq.det()
	if det == 0 {
		return 0, 0
	}

	a := eq.matrix[3]*px - eq.matrix[1]*py
	b := eq.matrix[0]*py - eq.matrix[2]*px

	if a%det != 0 || b%det != 0 {
		return 0, 0
	}
	return a / det, b / det
}

func parseData() (equations []equation) {
	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		text := scanner.Text()
		if text == "" {
			// Checking for the double newline break
			if !scanner.Scan() {
				break
			}
			text = scanner.Text()
		}
		for i := 0; i < 2; i++ {
			scanner.Scan()
			text += scanner.Text()
		}
		var (
			ax, bx, ay, by, px, py int
		)
		fmt.Sscanf(text, "Button A: X+%d, Y+%dButton B: X+%d, Y+%dPrize: X=%d, Y=%d", &ax, &ay, &bx, &by, &px, &py)
		eq := equation{[]int{ax, bx, ay, by}, []int{px, py}}
		equations = append(equations, eq)
	}
	return
}

func solve(equations []equation, part2 bool) int {
	tokens := 0
	for _, eq := range equations {
		if part2 {
			eq.prizes[0] += 10000000000000
			eq.prizes[1] += 10000000000000
		}
		a, b := eq.solve()
		tokens += a*3 + b
	}
	return tokens
}

func main() {
	equations := parseData()
	fmt.Println("Part 1: ", solve(equations, false))
	fmt.Println("Part 2: ", solve(equations, true))
}
