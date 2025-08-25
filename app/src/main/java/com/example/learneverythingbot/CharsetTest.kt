package com.example.learneverythingbot

import android.os.Build
import androidx.annotation.RequiresApi
import java.nio.charset.Charset
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun main() {
    System.setOut(PrintStream(FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8))
    System.setErr(PrintStream(FileOutputStream(FileDescriptor.err), true, StandardCharsets.UTF_8))

    println("Default charset: " + Charset.defaultCharset())
    println("file.encoding: " + System.getProperty("file.encoding"))
    println("sun.stdout.encoding: " + System.getProperty("sun.stdout.encoding"))
    println("sun.stderr.encoding: " + System.getProperty("sun.stderr.encoding"))
    println("áéíóú ç ãõ ÁÉÍÓÚ Ç ÃÕ — ok")
}