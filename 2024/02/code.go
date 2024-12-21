package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strconv"
	"strings"
)

func diff(a, b int) int {
	if a < b {
		return b - a
	}
	return a - b
}

func parseData() [][]int {
	var data [][]int
	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		text := scanner.Text()
		if text == "" {
			break
		}
		digits := strings.Split(text, " ")
		row := make([]int, 0, len(digits))
		for _, raw := range digits {
			v, err := strconv.Atoi(raw)
			if err != nil {
				log.Print(err)
				os.Exit(1)
			}
			row = append(row, v)
		}
		data = append(data, row)
	}
	return data
}

func isIncreasing(row []int) bool {
	score := 0
	for i, v := range row[1:] {
		score += (v - row[i])
	}
	return score > 0
}

func absDiffInt(x int, y int) int {
	if x > y {
		return x - y
	}
	return y - x
}

func main() {
	reports := parseData()

	safeReports := 0
	for _, report := range reports {
		increasing := isIncreasing(report)
		safe := true
		for i, v := range report[1:] {
			diff := absDiffInt(v, report[i])
			if diff < 1 || diff > 3 {
				safe = false
				break
			}
			if increasing {
				if v <= report[i] {
					safe = false
					break
				}
			} else {
				if v >= report[i] {
					safe = false
					break
				}
			}
		}
		if safe {
			safeReports++
		}
	}

	fmt.Println(safeReports)
}
