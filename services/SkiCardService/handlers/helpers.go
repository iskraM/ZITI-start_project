package handlers

import (
	"math/rand"
)

// function to generate a random string of given length and character set
func randString(length int, charset string) string {
	b := make([]byte, length)
	for i := range b {
		b[i] = charset[rand.Intn(len(charset))]
	}
	return string(b)
}
