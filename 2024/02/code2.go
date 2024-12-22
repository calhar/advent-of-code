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

func safeStep(increasing bool, cur int, prev int) bool {
	diff := absDiffInt(cur, prev)
	if diff < 1 || diff > 3 {
		return false
	}
	if increasing {
		if cur <= prev {
			return false
		}
	} else {
		if cur >= prev {
			return false
		}
	}
	return true
}

func isSafe(row []int, increasing bool) bool {
	errorAt := -1
	maybeSkip := false
	for i, v := range row[1:] {
		if maybeSkip {
			if !safeStep(increasing, v, row[i]) && !safeStep(increasing, v, row[i-1]) {
				return false
			} else {
				maybeSkip = false
			}
		} else if errorAt != -1 && errorAt == i-1 {
			if !safeStep(increasing, v, row[i-1]) {
				return false
			}
		} else if !safeStep(increasing, v, row[i]) {
			if errorAt != -1 {
				return false
			}
			errorAt = i
			if i == 0 || safeStep(increasing, v, row[i-1]) {
				maybeSkip = true
			}
		}
	}
	return true
}

func main() {
	reports := parseData()

	safeReports := 0
	for _, report := range reports {
		increasing := isIncreasing(report)
		newSafe := isSafe(report, increasing)
		if newSafe {
			safeReports++
		}
	}

	fmt.Println(safeReports)
}
