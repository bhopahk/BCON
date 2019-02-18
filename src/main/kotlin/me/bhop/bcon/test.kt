package me.bhop.bcon

import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import me.bhop.bcon.node.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
//    val parent: Node = OrphanNode()
//
//    val arr = ArrayNode("", parent = parent)
//    arr.add(StringNode("coolname", parent = parent, value = "Test"))
//
//    val idk: PrimitiveNode = StringNode("c", parent = parent, value = "Test2")
//
//    val b: NodeBuilder = NodeBuilder().id("")
//
//    val x: OrphanNode
//    val reader = BconReader()
//
//    val root = OrphanNode()
//    reader.parseCategory(root, File("./bcon.conf").readLines().joinToString("\n"))
//
//    println("Children")
//    for (child in root.children)
//        println("Child: ${child.id} | $child")
    val x = 10
    println("Running $x trials...")
    println("Bcon File: https://hastebin.com/poxikidigo.pl")
    println("Json File: https://hastebin.com/ofuyuxugiz.json")
    println("--------------------")
    var bTotal: Long = 0
    var jTotal: Long = 0
    for (i in 1..x) {
        val bconStart = System.nanoTime()
        Files.newBufferedReader(Paths.get("./bcon.conf")).use {
            val bcon = BconReader()
            val root = OrphanNode()
            bcon.parseCategory(root, it.readText())
        }
        val bconTime = System.nanoTime()-bconStart
        bTotal += bconTime

        val jsonStart = System.nanoTime()
        Files.newBufferedReader(Paths.get("./json.json")).use {
            val json = JsonParser()
            val root = json.parse(it.readText()).asJsonObject
        }
        val jsonTime = System.nanoTime()-jsonStart
        jTotal += jsonTime

        println("\t$i:")
        println("\tBcon: ${bconTime}ns")
        println("\tJson: ${jsonTime}ns")
        println("\tDiff: ${jsonTime-bconTime}ns")
        System.gc()
    }

    bTotal /= x
    jTotal /= x
    println("Results:")
    println("Bcon Average: ${bTotal}ns | ${bTotal/1000000}ms")
    println("Json Average: ${jTotal}ns | ${jTotal/1000000}ms")
    println("Difference: ${bTotal-jTotal}ns | ${(bTotal-jTotal)/1000000}ms (if positive, Bcon is slower)" )



//    val gson = JsonReader()

}

