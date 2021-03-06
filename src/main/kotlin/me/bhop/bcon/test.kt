package me.bhop.bcon

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import com.google.gson.stream.JsonReader
import me.bhop.bcon.lexer.BconLexer
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

//    val x = 10
//    println("Running $x trials...")
//    println("Bcon File: https://hastebin.com/poxikidigo.pl")
//    println("Json File: https://hastebin.com/ofuyuxugiz.json")
//    println("--------------------")
//    var bTotal: Long = 0
//    var jTotal: Long = 0
//    for (i in 1..x) {
//        val bconStart = System.nanoTime()
//        Files.newBufferedReader(Paths.get("./bcon.conf")).use {
//            val bcon = BconReader()
//            val root = OrphanNode()
//            bcon.parseCategory(root, it.readText())
//        }
//        val bconTime = System.nanoTime()-bconStart
//        bTotal += bconTime
//
//        val jsonStart = System.nanoTime()
//        Files.newBufferedReader(Paths.get("./json.json")).use {
//            val json = JsonParser()
//            val root = json.parse(it.readText()).asJsonObject
//        }
//        val jsonTime = System.nanoTime()-jsonStart
//        jTotal += jsonTime
//
//        println("\t$i:")
//        println("\tBcon: ${bconTime}ns")
//        println("\tJson: ${jsonTime}ns")
//        println("\tDiff: ${jsonTime-bconTime}ns")
//        System.gc()
//    }
//
//    bTotal /= x
//    jTotal /= x
//    println("Results:")
//    println("Bcon Average: ${bTotal}ns | ${bTotal/1000000}ms")
//    println("Json Average: ${jTotal}ns | ${jTotal/1000000}ms")
//    println("Difference: ${bTotal-jTotal}ns | ${(bTotal-jTotal)/1000000}ms (if positive, Bcon is slower)" )

//    val jsonStart = System.nanoTime()
//    val json = JsonParser()
//    val root = json.parse(test2).asJsonObject
//    val jsonTime = System.nanoTime()-jsonStart
//    println("GSON : ${jsonTime/1000000}ms")
//
//    val before = System.nanoTime()
//    //Files.newBufferedReader(Paths.get("./bcon.conf")).use {  }
//    val lexer = BconLexer(test)
//    lexer.lex()
//    println("BCON NEW: ${(System.nanoTime()-before)/1000000}ms")
//
//
////    Files.newBufferedReader(Paths.get("./json.json")).use {
////        val json = JsonParser()
////
////    }
//
//
//    val bconStart = System.nanoTime()
//    val root2 = OrphanNode()
//    val bcon = BconReader()
//    bcon.parseCategory(root2, test)
//    val bconTime = System.nanoTime()-bconStart
//    println("BCON OLD : ${bconTime/1000000}ms")
//    Files.newBufferedReader(Paths.get("./bcon.conf")).use {
//
//
//
//    }

//    val lexer = BconLexer("key:\"value\"")
//    lexer.lex()
//    while (lexer.hasNext())
//        println(lexer.next().toString().replace("\n", "\\n"))




    val root = BconParser().fromBcon()

    println("\n\n\n")
    for (node in root.children) {
        println("$node")
    }
}


val test: String = "\n" +
        "# comment up here too!\n" +
        "category {\n" +
        "  option-in-category: \"I am an option with both spaces and illegal characters!\"\n" +
        "  option-2: IAmAnOptionWithNoSpacesOrIllegalCharacters\n" +
        "\n" +
        "  subcategory {\n" +
        "    this: \"counts as a subcategory! It would be accessed with the path 'category.subcategory.this'\"\n" +
        "  }\n" +
        "  \"this value\": \"that value\"\n" +
        "\n" +
        "  cat-list: [\n" +
        "    1\n" +
        "    5\n" +
        "    6\n" +
        "    3\n" +
        "  ]\n" +
        "\n" +
        "  second-sub {\n" +
        "    i: \"am another cool value for testing...\"\n" +
        "  }\n" +
        "}\n" +
        "\n" +
        "# 123c awd awdaw\n" +
        "optionInRoot: ABCDEFG\n" +
        "option2InRoot: 10\n" +
        "\n" +
        "list: [\n" +
        "  1\n" +
        "  2\n" +
        "  3\n" +
        "  4\n" +
        "  5\n" +
        "]\n" +
        "\n" +
        "# WHY ARE THESE NOT WORKING!!!!\n" +
        "list2: [\"test1\", \"test2\", \"test3\"]\n" +
        "\n" +
        "# I am a comment!\n" +
        "# I too, am a comment\n" +
        "list3: [\n" +
        "  1,\n" +
        "  2,\n" +
        "  3,\n" +
        "  4,\n" +
        "  5\n" +
        "]\n" +
        "\n" +
        "category.option-3:\"This is an option in the the above category.\"\n" +
        "\n" +
        "# comments for days\n" +
        "category.subcategory_2: {\n" +
        "  this: \"is a another sub category inside the 'category' category.\"\n" +
        "  otherList: [\n" +
        "    \"I\",\n" +
        "    \"feel\"\n" +
        "    \"like\"\n" +
        "    \"this\"\n" +
        "    \"will\"\n" +
        "    \"not\"\n" +
        "    \"work\"\n" +
        "  ]\n" +
        "}\n" +
        "\n" +
        "# :O another comment\n" +
        "it-is-also: \"valid to have options at the root of the project.\"\n" +
        "\n" +
        "additionally { it: \"is ok to have the entire file on one line!\", however: \"it does require a comma to separate the values if they are on the same line.\", even-further { it: \"is ok to have sub categories too\" } }\n" +
        "\n" +
        "another-key {\n" +
        "is: \"that indentation means nothing!\",\n" +
        "  additionally: \"it is ok to use commas if you want\"\n" +
        "  but: \"you definately do not have to use them...\"\n" +
        "}"
val test2 = "{\n" +
        "  \"category\": {\n" +
        "    \"option-in-category\": \"I am an option with both spaces and illegal characters!\",\n" +
        "    \"option-2\": \"IAmAnOptionWithNoSpacesOrIllegalCharacters\",\n" +
        "    \"subcategory\": {\n" +
        "      \"this\": \"counts as a subcategory! It would be accessed with the path 'category.subcategory.this'\"\n" +
        "    },\n" +
        "    \"cat-list\": [\n" +
        "      1,\n" +
        "      5,\n" +
        "      6,\n" +
        "      3\n" +
        "    ],\n" +
        "    \"second-sub\": {\n" +
        "      \"i\": \"am another cool value for testing...\"\n" +
        "    },\n" +
        "    \"option-3\": \"This is an option in the the above category.\",\n" +
        "    \"subcategory_2\": {\n" +
        "      \"this\": \"is a another sub category inside the 'category' category.\",\n" +
        "      \"otherList\": [\n" +
        "        \"I\",\n" +
        "        \"feel\",\n" +
        "        \"like\",\n" +
        "        \"this\",\n" +
        "        \"will\",\n" +
        "        \"not\",\n" +
        "        \"work\"\n" +
        "      ]\n" +
        "    }\n" +
        "  },\n" +
        "  \"optionInRoot\": \"ABCDEFG\",\n" +
        "  \"option2InRoot\": 10,\n" +
        "  \"list\": [\n" +
        "    1,\n" +
        "    2,\n" +
        "    3,\n" +
        "    4,\n" +
        "    5\n" +
        "  ],\n" +
        "  \"list2\": [\"test1\", \"test2\", \"test3\"],\n" +
        "  \"list3\": [\n" +
        "    1,\n" +
        "    2,\n" +
        "    3,\n" +
        "    4,\n" +
        "    5\n" +
        "  ],\n" +
        "  \"it-is-also\": \"valid to have options at the root of the project.\",\n" +
        "  \"additionally\": { \"it\": \"is ok to have the entire file on one line!\", \"however\": \"it does require a comma to separate the values if they are on the same line.\", \"even-further\": { \"it\": \"is ok to have sub categories too\" } },\n" +
        "  \"another-key\": {\n" +
        "    \"is\": \"that indentation means nothing!\",\n" +
        "    \"additionally\": \"it is ok to use commas if you want\",\n" +
        "    \"but\": \"you definately do not have to use them...\"\n" +
        "  }\n" +
        "}"