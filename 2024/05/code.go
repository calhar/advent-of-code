package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strconv"
	"strings"
)

type set map[int]bool

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

func validUpdate(validChains map[int]set, update []int) bool {
	encountered := make(set)
	for _, val := range update {
		invalidAfter := validChains[val]
		intersection := intersect(encountered, invalidAfter)
		if len(intersection) > 0 {
			return false
		}
		encountered[val] = true
	}
	return true
}

func buildValidLinksMap(scanner *bufio.Scanner) map[int]set {
	out := make(map[int]set)
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
		tmp := strings.Split(text, ",")
		update := make([]int, 0, len(tmp))
		for _, raw := range tmp {
			v, err := strconv.Atoi(raw)
			if err != nil {
				log.Print(err)
				os.Exit(1)
			}
			update = append(update, v)
		}
		out = append(out, update)
	}
	return out
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)

	validChains := buildValidLinksMap(scanner)

	updates := buildUpdates(scanner)

	total := 0
	for _, update := range updates {
		if validUpdate(validChains, update) {
			total += update[len(update)/2]
		}
	}

	fmt.Println(total)
}
