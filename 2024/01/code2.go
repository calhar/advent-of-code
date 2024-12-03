package main

import (
	"bufio"
	"fmt"
	"os"
)

func main() {
	scanner := bufio.NewScanner(os.Stdin)

	var list1 []int
	var list2 = map[int]int{}

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
		list2[second] += 1
	}

	if err := scanner.Err(); err != nil {
		fmt.Println("Error:", err)
	}

	similarity := 0

	for i := range list1 {
		similarity += list1[i] * list2[list1[i]]
	}
	fmt.Println(similarity)
}
