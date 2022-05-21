package com.emmanuel.dijitalgaraj.quiz

import java.math.BigInteger
import java.security.MessageDigest

/**
 * This function would take in a hash value and an email (salt) and return the expected
 * hashed email value through brute-force
 **/
public fun searchHash(hashString: String, email: String): String {
    val mdLength = 32
    var count = (hashString.length)/mdLength
    var currentBatch = 0
    var currentWord = ""

    while (count != 0){
        val currentHash: String = hashString.subSequence(currentBatch, currentBatch + mdLength).toString()
        val bestDuo: String = checkSum(currentHash, currentWord, email)
        if (bestDuo == "~"){
            break
        }
        currentWord += bestDuo
        currentBatch += mdLength
        --count
    }
    return currentWord
}

/**
 * Function to return md5 hash
 */
private fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}

/**
 * Function to return custom hash String
 */
private fun hash(x: String, email: String): String {
    return md5((md5(email)+x+md5(x)))
}

/**
 *  Function to check for the next two added letters
 */
private fun checkSum(currentHash: String, currentWord: String, email: String): String {
    /// Create character list for bruteForce
    val charList = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '-', '—','.', '_', '@', ',', ')', '(')
    var i = 0 // First Counter
    var j = 0 // Second Counter
//        println("Current Word: ${currentWord}")

    while (i < charList.size){
        while (j < charList.size){
            val charOne = charList[i]
            val charTwo = charList[j]
            /// Append the two characters to the current word
//                println("${charOne}, ${charTwo}")
            val word: String = StringBuilder(currentWord).append(charOne).append(charTwo).toString()
//                println(word)
            /// Generate a hash of the word
            val wordHash: String = hash(word, email)
            /// Compare the word to the currentHash
            if (wordHash == currentHash) {
//                    println(word)
                // return the two characters as a string
                return StringBuilder("").append(charOne).append(charTwo).toString()
            }
            /// Increment secondCounter
            j++
        }
        /// Reset j (secondCounter) and increment firstCounter
        j = 0
        i++
    }
    return "~"
}

/**
 * Function to return the salted check of a value, it can take a string and an email address
 * using a default break length of 2 (assuming the string to be even).
 */
private fun reverseCheckSum(input: String, email: String): String{
    val breakLength = 2
    var currentBatch = 0
    var count = input.length/2
    var word = ""
    var hashWord = ""

    while (count != 0) {
        val currentWord: String = input.subSequence(currentBatch, currentBatch + breakLength).toString()
        currentBatch += breakLength
        word += currentWord
        hashWord += hash(word, email)
        println(hashWord)
        --count
    }
    return hashWord

}