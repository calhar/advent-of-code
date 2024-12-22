package main

import (
	"bufio"
	"fmt"
	"os"
)

var xmas = "XMAS"
var samx = "SAMX"

func parseData() []string {
	var data []string
	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		text := scanner.Text()
		if text == "" {
			break
		}
		data = append(data, text)
	}
	return data
}

func checkHorizontal(wordsearch []string, i int, j int, word string) int {
	if j+len(word) > len(wordsearch[i]) {
		return 0
	}
	for offset := 1; offset < len(word); offset++ {
		if word[offset] != wordsearch[i][j+offset] {
			return 0
		}
	}
	return 1
}

func checkVertical(wordsearch []string, i int, j int, word string) int {
	if i+len(word) > len(wordsearch) {
		return 0
	}
	for offset := 1; offset < len(word); offset++ {
		if word[offset] != wordsearch[i+offset][j] {
			return 0
		}
	}
	return 1
}

func checkDiagRight(wordsearch []string, i int, j int, word string) int {
	if i+len(word) > len(wordsearch) || j+len(word) > len(wordsearch[i]) {
		return 0
	}
	for offset := 1; offset < len(word); offset++ {
		if word[offset] != wordsearch[i+offset][j+offset] {
			return 0
		}
	}
	return 1
}

func checkDiagLeft(wordsearch []string, i int, j int, word string) int {
	if i+len(word) > len(wordsearch) || j-len(word)+1 < 0 {
		return 0
	}
	for offset := 1; offset < len(word); offset++ {
		if word[offset] != wordsearch[i+offset][j-offset] {
			return 0
		}
	}
	return 1
}

func main() {
	total := 0

	wordsearch := parseData()
	for i, row := range wordsearch {
		for j, c := range row {
			if c == 'X' || c == 'S' {
				word := xmas
				if c == 'S' {
					word = samx
				}
				total += checkHorizontal(wordsearch, i, j, word)
				total += checkVertical(wordsearch, i, j, word)
				total += checkDiagRight(wordsearch, i, j, word)
				total += checkDiagLeft(wordsearch, i, j, word)
			}
		}
	}

	fmt.Println(total)
}
