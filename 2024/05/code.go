package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"os"
)

type set map[int]bool
type rules map[int]set

func intersect(set1 set, set2 set) set {
	intersection := make(set)
	if len(set1) > len(set2) {
		set2, set1 = set1, set2
	}
	for k := range set1 {
		if set2[k] {
			intersection[k] = true
		}
	}
	return intersection
}

func validUpdate(rules rules, update []int) bool {
	encountered := make(set)
	for _, val := range update {
		invalidAfter := rules[val]
		intersection := intersect(encountered, invalidAfter)
		if len(intersection) > 0 {
			return false
		}
		encountered[val] = true
	}
	return true
}

func buildValidLinksMap(scanner *bufio.Scanner) rules {
	out := make(rules)
	for scanner.Scan() {
		text := scanner.Text()
		if text == "" {
			break
		}
		var (
			first  int
			second int
		)
		if _, err := fmt.Sscanf(text, "%d|%d", &first, &second); err != nil {
			fmt.Println("Error:", err)
			os.Exit(-1)
		}
		if _, ok := out[first]; !ok {
			out[first] = make(set)
		}
		out[first][second] = true
	}
	return out
}

func buildUpdates(scanner *bufio.Scanner) [][]int {
	out := make([][]int, 0)
	for scanner.Scan() {
		text := scanner.Text()
		if text == "" {
			break
		}
		var update []int
		json.Unmarshal([]byte("["+text+"]"), &update)
		out = append(out, update)
	}
	return out
}

func parseData() (rules, [][]int) {
	scanner := bufio.NewScanner(os.Stdin)
	rules := buildValidLinksMap(scanner)
	updates := buildUpdates(scanner)
	return rules, updates
}

func main() {
	rules, updates := parseData()

	total := 0
	for _, update := range updates {
		if validUpdate(rules, update) {
			total += update[len(update)/2]
		}
	}

	fmt.Println(total)
}
