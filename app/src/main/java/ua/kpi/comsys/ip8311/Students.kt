package ua.kpi.comsys.ip8311

import java.util.*

fun parseStudents(studentsStr : String) : MutableMap<String, String> {
    var relationMap = mutableMapOf<String, String>()
    for (pair in studentsStr.split("; ")) {
        val pair = pair.split(" - ")
        relationMap[pair[0]] = pair[1]
    }
    return relationMap
}

fun createArrayOfMarks(maxVal: Int) : Array<Int> {
    var marks = Array<Int>(8, {m -> randomValue(maxVal)})
    marks[7] = randomValue(16)
    return marks
}

fun randomValue(maxValue: Int) : Int {
    when((Math.random() * 7).toInt()) {
        1 -> return (Math.ceil(maxValue.toFloat() * 0.7)).toInt()
        2 -> return (Math.ceil(maxValue.toFloat() * 0.9)).toInt()
        3, 4, 5 -> return maxValue
        else -> return 0
    }
}

fun main() {
    //Task 1
    val studentsStr : String = "Дмитренко Олександр - ІП-84; Матвійчук Андрій - ІВ-83; Лесик Сергій - ІО-82; Ткаченко Ярослав - ІВ-83;" +
            " Аверкова Анастасія - ІО-83; Соловйов Даніїл - ІО-83; Рахуба Вероніка - ІО-81; Кочерук Давид - ІВ-83; Лихацька Юлія - ІВ-82; Головенець Руслан" +
            " - ІВ-83; Ющенко Андрій - ІО-82; Мінченко Володимир - ІП-83; Мартинюк Назар - ІО-82; Базова Лідія - ІВ-81; Снігурець Олег - ІВ-81;" +
            " Роман Олександр - ІО-82; Дудка Максим - ІО-81; Кулініч Віталій - ІВ-81; Жуков Михайло - ІП-83; Грабко Михайло - ІВ-81; Іванов Володимир" +
            " - ІО-81; Востриков Нікіта - ІО-82; Бондаренко Максим - ІВ-83; Скрипченко Володимир - ІВ-82; Кобук Назар - ІО-81; Дровнін Павло - ІВ-83;" +
            " Тарасенко Юлія - ІО-82; Дрозд Світлана - ІВ-81; Фещенко Кирил - ІО-82; Крамар Віктор - ІО-83; Іванов Дмитро - ІВ-82"
    val mappedGroups = parseStudents(studentsStr)
    val studentsGroups = mutableMapOf<String, MutableList<String>>()

    for((name, group) in mappedGroups) {
        if(!studentsGroups.containsKey(group)) {
            studentsGroups[group] = mutableListOf(name)
        } else {
            studentsGroups[group]?.add(name)
        }
    }
    for((group, _) in studentsGroups) {
        studentsGroups[group]?.sort()
    }

    println("Завдання 1")
    println(studentsGroups)
    println()

    //Task 2
    var studentPoints = mutableMapOf<String, MutableList<MutableMap<String, Array<Int>>>>()

    for ((group, list) in studentsGroups) {
        var first = true
        for(name in list) {
            val marks = createArrayOfMarks(12)
            //If met the group for the first time
            if(first) {
                first = false
                studentPoints[group] = mutableListOf(mutableMapOf(name to marks))
            } else {
                studentPoints[group]?.add(mutableMapOf(name to marks))
            }
        }
    }

    println("Завдання 2")
    println("Масиви відображаються як ссилки на них у Джава")
    println(studentPoints)
    println()

    //Task 3
    println("Завдання 3")
    var sumPoints = mutableMapOf<String, MutableList<Map<String, Int>>>()

    for((group, list) in studentPoints) {
        var first = true
        for(pair in list) {
            for((name, pointsArray) in pair) {
                var sum = pointsArray.sum()
                if(first) {
                    first = false
                    sumPoints[group] = mutableListOf(mapOf(name to sum))
                } else {
                    sumPoints[group]?.add(mapOf(name to sum))
                }
            }
        }
    }

    println(sumPoints)
    println()

    //Task 4
    println("Завдання 4")
    var groupAvg = mutableMapOf<String, Double>()
    for ((group, list) in sumPoints) {
        var counter = 0.0
        for(pair in list) {
            for((name, mark) in pair) {
                counter += mark
            }
        }
        groupAvg[group] = counter/list.size
    }
    println(groupAvg)
    println()

    //Task 5
    println("Завдання 5")

    var passedPerGroup = mutableMapOf<String, MutableList<String>>()
    for((group, list) in sumPoints) {
        var first = true
        for(pair in list) {
            for((name, mark) in pair) {
                if(mark >= 60) {
                    if(first) {
                        first = false
                        passedPerGroup[group] = mutableListOf(name)
                    } else {
                        passedPerGroup[group]?.add(name)
                    }
                }
            }
        }
    }
    println(passedPerGroup)
}