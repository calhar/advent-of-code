package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

type file struct {
	fileId int
	start  int
	size   int
}

type emptyBlock struct {
	start int
	size  int
}

func (file *file) checksum() (checksum int) {
	for i := file.start; i < file.start+file.size; i++ {
		checksum += i * file.fileId
	}
	return
}

func (file *file) moveToEmptyBlock(block *emptyBlock) {
	file.start = block.start
	block.start += file.size
	block.size -= file.size
}

func parseData() (files []file, emptyBlocks []emptyBlock) {
	scanner := bufio.NewScanner(os.Stdin)

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

		offset := 0
		files = make([]file, 0, (len(compressed)+1)/2)
		emptyBlocks = make([]emptyBlock, 0, len(compressed)/2)
		for i, fragment := range compressed {
			if i%2 == 0 {
				files = append(files, file{i / 2, offset, fragment})
			} else {
				emptyBlocks = append(emptyBlocks, emptyBlock{offset, fragment})
			}
			offset += fragment
		}
	}

	return
}

func main() {
	files, emptyBlocks := parseData()

	checksum := 0

	for i := len(files) - 1; i >= 0; i-- {
		for j := 0; j < len(emptyBlocks); j++ {
			if emptyBlocks[j].start >= files[i].start {
				break
			}
			if emptyBlocks[j].size >= files[i].size {
				files[i].moveToEmptyBlock(&emptyBlocks[j])
				break
			}
		}
		checksum += files[i].checksum()
	}
	fmt.Println(checksum)
}
