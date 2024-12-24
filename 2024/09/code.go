package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

const empty int = -1

type disk []int

func parseData() disk {
	scanner := bufio.NewScanner(os.Stdin)
	var out disk
	for scanner.Scan() {
		text := scanner.Text()
		if text == "" {
			break
		}
		compressed := make([]int, len(text), len(text))
		size := 0
		for i, c := range text {
			compressed[i], _ = strconv.Atoi(string(c))
			size += compressed[i]
		}

		out = make(disk, 0, size)
		for i, fragment := range compressed {
			var contents int
			if i%2 == 0 {
				contents = i / 2
			} else {
				contents = empty
			}
			for j := 0; j < fragment; j++ {
				out = append(out, contents)
			}
		}
	}

	return out
}

func main() {
	disk := parseData()

	start, end := 0, len(disk)-1

	checksum := 0

	for {
		for disk[start] != empty {
			checksum += start * disk[start]
			start++
		}
		for disk[end] == empty {
			end--
		}
		if end <= start {
			break
		}

		disk[start] = disk[end]
		disk[end] = -1
		checksum += start * disk[start]
		start++
		end--
	}
	fmt.Println(checksum)
}
