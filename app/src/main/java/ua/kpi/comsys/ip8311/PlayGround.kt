package ua.kpi.comsys.ip8311

import kotlin.math.min

enum class Direction {
    longitude,
    latitude
}

class CoordinateIK{
    constructor(dir: Direction = Direction.latitude, deg: Int = 0, min: UInt = 0u, sec: UInt = 0u) {
        if (dir == Direction.latitude) {
            if (deg < -90 || deg > 90) {
                error("not allowed")
            }
        } else {
            if (deg < -180 || deg > 180) {
                error("not allowed2")
            }
        }
        if (min < 0u || min > 59u || sec < 0u || sec > 59u) error("not allowed3")
        direction = dir
        degrees = deg
        minutes = min
        seconds = sec
    }
    var direction: Direction
    var degrees: Int
    var minutes: UInt
    var seconds: UInt

    fun getDirection() : String {
        when(direction) {
            Direction.latitude -> {
                if (degrees > 0) return "N" else return "S"
            }
            Direction.longitude -> {
                if(degrees > 0) return "E" else return "W"
            }
        }
    }
    companion object {
        fun method4(coor1 : CoordinateIK, coor2 : CoordinateIK) : CoordinateIK? {
            if (coor1.direction != coor2.direction) return null
            val avgDeg = (coor1.degrees + coor2.degrees) / 2
            val avgSec = (coor1.seconds + coor2.seconds) / 2u
            val avgMin = (coor1.minutes + coor2.minutes) / 2u
            return CoordinateIK(coor1.direction, avgDeg, avgMin, avgSec)
        }
    }
}

fun CoordinateIK.method1_xyzZ() : String {
    return "$degrees°$minutes′$seconds″ ${getDirection()}"
}

fun CoordinateIK.decimate() : Float {
    if(degrees >= 0) return degrees.toFloat() + minutes.toFloat()/60 + seconds.toFloat()/3600 else
        return degrees.toFloat() - minutes.toFloat()/60 - seconds.toFloat()/3600
}

fun CoordinateIK.method2_xxx() : String {
    return "${decimate()}° ${getDirection()}"
}

fun CoordinateIK.method3(coor : CoordinateIK) : CoordinateIK? {
    if (direction != coor.direction) return null
    val avgDeg = (degrees + coor.degrees) / 2
    val avgSec = (seconds + coor.seconds) / 2u
    val avgMin = (minutes + coor.minutes) / 2u
    return CoordinateIK(direction, avgDeg, avgMin, avgSec)
}

fun main () {
    var koi = CoordinateIK(Direction.longitude, 15, 15u, 15u)
    var koi2 = CoordinateIK()
    var koi3 = CoordinateIK(Direction.longitude, 25, 30u, 35u)
    println(koi.method1_xyzZ())
    println(koi2.method1_xyzZ())
    println()
    println(koi.method2_xxx())
    println(koi2.method2_xxx())
    println()
    var ko = koi.method3(koi3)
    println('Method 4 is static')
    var koo = CoordinateIK.method4(koi, koi3)
}