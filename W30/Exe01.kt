//fun findKthLarge(arr: IntArray, k: Int): Int {
//    // k次冒泡
//    var i = 0
//    while (i < k) {
//        for (index in arr.indices-arr.lastIndex) {
//            if (arr[index] > arr[index+1]) {
//                var temp = arr[index]
//                arr[index] = arr[index+1]
//                arr[index+1] = temp
//            }
//        }
//        i ++
//    }
//    return arr[arr.size-k]
//}

//  快排划分
fun partition(arr: IntArray, left: Int, right: Int): Int {
    val base = arr[left]
    var l = left
    var r = right
    while (l < r) {
        while (l < r && arr[r] >= base) r --;
        if (arr[r] < base) {
            arr[l] = arr[r]
            l++
        }
        while (l < r && arr[l] <= base) l ++;
        if (arr[l] > base) {
            arr[r] = arr[l]
            r --
        }
    }
    arr[l] = base
    return l
}

fun findKthLargeWithQS(arr: IntArray, left: Int, right: Int, k: Int): Int {
    var index = partition(arr, left, right)
    if (index == arr.size-k) return arr[index]
    else if (index < arr.size-k) return findKthLargeWithQS(arr, index+1, right, k)
    else return findKthLargeWithQS(arr, left, index-1, k)
}

fun main() {
    val arr = intArrayOf(3, 2, 3, 1, 2, 4, 3, 5, 6)
    val k = 9
    println(findKthLargeWithQS(arr, 0, arr.size-1, k))
//    println(findKthLarge(arr, k))
}