package com.bitprobe.phstore

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {

    @Bean
    fun cmdRunner(): CommandLineRunner = geodeRunner()
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}