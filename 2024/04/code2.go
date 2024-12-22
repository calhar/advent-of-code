package main

import (
	"bufio"
	"fmt"
	"os"
)

var mas = "MAS"
var sam = "SAM"

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

func checkDiagLeft(wordsearch []string, i int, j int) bool {
	word := mas
	if wordsearch[i][j] == 'S' {
		word = sam
	}
	for offset := 0; offset < len(word); offset++ {
		if wordsearch[i+offset][j+offset] != word[offset] {
			return false
		}
	}
	return true
}

func checkReverseDiag(wordsearch []string, i int, j int) bool {
	word := mas
	if wordsearch[i][j+2] == 'S' {
		word = sam
	}
	for offset := 0; offset < len(word); offset++ {
		if wordsearch[i+offset][j+2-offset] != word[offset] {
			return false
		}
	}
	return true
}

func main() {
	total := 0

	wordsearch := parseData()
	for i, row := range wordsearch[2:] {
		for j, c := range row[2:] {
			if c == 'M' || c == 'S' {
				if checkDiagLeft(wordsearch, i, j) && checkReverseDiag(wordsearch, i, j) {
					total++
				}

			}
		}
	}

	fmt.Println(total)
}
