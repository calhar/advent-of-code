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

func isSafe(row []int, increasing bool) bool {
	safe := true
	for i, v := range row[1:] {
		diff := absDiffInt(v, row[i])
		if diff < 1 || diff > 3 {
			safe = false
			break
		}
		if increasing {
			if v <= row[i] {
				safe = false
				break
			}
		} else {
			if v >= row[i] {
				safe = false
				break
			}
		}
	}
	return safe
}

func main() {
	reports := parseData()

	safeReports := 0
	for _, report := range reports {
		increasing := isIncreasing(report)
		if isSafe(report[1:], increasing) {
			safeReports++
			continue
		}
		if isSafe(report[:len(report)-1], increasing) {
			safeReports++
			continue
		}
		for i := 1; i < len(report)-1; i++ {
			altReport := make([]int, 0, len(report)-1)
			altReport = append(altReport, report[:i]...)
			altReport = append(altReport, report[i+1:]...)
			if isSafe(altReport, increasing) {
				safeReports++
				break
			}
		}
	}

	fmt.Println(safeReports)
}
