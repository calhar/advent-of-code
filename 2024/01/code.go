package main

import (
	"bufio"
	"fmt"
	"os"
	"slices"
)

func diff(a, b int) int {
	if a < b {
		return b - a
	}
	return a - b
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)

	var (
		list1 []int
		list2 []int
	)

	for scanner.Scan() {
		text := scanner.Text()
		if text == "" {
			break
		}
		var (
			first  int
			second int
		)
		if _, err := fmt.Sscanf(text, "%d\t%d", &first, &second); err != nil {
			fmt.Println("Error:", err)
			os.Exit(-1)
		}

		list1 = append(list1, first)
		list2 = append(list2, second)
	}

	if err := scanner.Err(); err != nil {
		fmt.Println("Error:", err)
	}

	slices.Sort(list1)
	slices.Sort(list2)

	totalDiff := 0

	for i := range list1 {
		totalDiff = totalDiff + diff(list1[i], list2[i])
	}
	fmt.Println(totalDiff)
}
