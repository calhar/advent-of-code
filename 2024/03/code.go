package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
)

func mul(command string) int {
	var (
		first  int
		second int
	)
	if _, err := fmt.Sscanf(command, "mul(%d,%d)", &first, &second); err != nil {
		fmt.Println("Error:", err)
		os.Exit(-1)
	}
	return first * second
}

func main() {
	total := 0
	r := regexp.MustCompile("mul\\([0-9]{1,3},[0-9]{1,3}\\)")
	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		line := scanner.Text()
		if line == "" {
			break
		}
		for _, cmd := range r.FindAllString(line, -1) {
			total += mul(cmd)
		}
	}

	fmt.Println(total)
}
