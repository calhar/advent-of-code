package main

import (
	"bufio"
	"fmt"
	"os"
)

type grid [][]int

var dirs [][]int = [][]int{{-1, 0}, {0, -1}, {1, 0}, {0, 1}}

func (grid *grid) contains(x int, y int) bool {
	return x >= 0 && x < len(*grid) && y >= 0 && y < len((*grid)[x])
}

func parseData() (grid grid) {
	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		text := scanner.Text()
		if text == "" {
			break
		}
		row := make([]int, 0, len(text))
		for _, c := range text {

			row = append(row, int(c)-48)
		}
		grid = append(grid, row)
	}
	return
}

func dfs(grid grid, x int, y int, height int) int {
	trails := 0
	for _, dir := range dirs {
		newX := x + dir[0]
		newY := y + dir[1]
		if grid.contains(newX, newY) {
			if grid[newX][newY] == height {
				if height == 9 {
					trails += 1
				} else {
					trails += dfs(grid, newX, newY, height+1)
				}
			}
		}
	}
	return trails
}

func main() {
	mountain := parseData()
	trails := 0
	for i := 0; i < len(mountain); i++ {
		for j := 0; j < len(mountain[i]); j++ {
			if mountain[i][j] == 0 {
				trails += dfs(mountain, i, j, 1)
			}
		}
	}
	fmt.Println(trails)
}
