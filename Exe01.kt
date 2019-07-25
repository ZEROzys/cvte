fun findKthLarge(arr: IntArray, k: Int): Int {
    // k次冒泡
    var i = 0
    while (i < k) {
        for (index in arr.indices-arr.lastIndex) {
            if (arr[index] > arr[index+1]) {
                var temp = arr[index]
                arr[index] = arr[index+1]
                arr[index+1] = temp
            }
        }
        i ++
    }
    return arr[arr.size-k]
}

fun main() {
    val arr = intArrayOf(3, 2, 3, 1, 2, 4, 5, 5, 6)
    val k = 4
    println(findKthLarge(arr, k))
}