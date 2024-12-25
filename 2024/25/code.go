package main

import (
	"bufio"
	"fmt"
	"os"
)

type lock struct {
	pins [5]int
}

type key struct {
	pins [5]int
}

func parseData() (locks []lock, keys []key) {
	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		text := scanner.Text()
		if text == "" {
			// Checking for the double newline break
			if !scanner.Scan() {
				break
			}
			text = scanner.Text()
		}
		isLock := text[0] == '#'
		pins := [5]int{0, 0, 0, 0, 0}
		for i := 0; i < 5; i++ {
			scanner.Scan()
			line := scanner.Text()
			for j, c := range line {
				if c == '#' {
					pins[j]++
				}
			}
		}
		// Ignorable line
		scanner.Scan()
		if isLock {
			locks = append(locks, lock{pins})
		} else {
			keys = append(keys, key{pins})
		}
	}
	return
}

func (lock *lock) fits(key *key) bool {
	for i := 0; i < 5; i++ {
		if lock.pins[i]+key.pins[i] > 5 {
			return false
		}
	}
	return true
}

func main() {
	locks, keys := parseData()

	groupedLocks := make(map[int][]lock)
	for _, lock := range locks {
		bin := groupedLocks[lock.pins[0]]
		bin = append(bin, lock)
		groupedLocks[lock.pins[0]] = bin
	}

	combinations := 0
	for _, key := range keys {
		for height := 0; height < 6-key.pins[0]; height++ {
			for _, lock := range groupedLocks[height] {
				if lock.fits(&key) {
					combinations++
				}
			}
		}
	}
	fmt.Println(combinations)
}
