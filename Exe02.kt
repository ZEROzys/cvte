import java.util.*

class StockSpanner (val arr: IntArray) {
    fun getSpannerArray(): IntArray {
        var spannerArray = IntArray(arr.size)
        spannerArray[0] = 1

        //  维护一个单调栈
        val stack = LinkedList<Pair<Int, Int>>()
        stack.add(0 to arr[0])
        for (i in arr.indices-0) {
            //  当前元素比栈顶元素大，栈顶元素出栈
            while (arr[i] > stack.peekLast().second) {
                stack.pollLast()
            }
            /*
             * 两种情况，当前元素与栈顶元素相等，则当前元素的股票跨度为栈顶元素的跨度+1
             * 当前元素比栈顶元素小，跨度为当前元素与栈顶元素的索引之差
             */
            if (arr[i] == stack.peekLast().second)
                spannerArray[i] = spannerArray[i-1] + 1
            else
                spannerArray[i] = i - stack.peekLast().first
            stack.offerLast(i to arr[i])
        }
        return spannerArray
    }
}


fun main() {
//    val arr = intArrayOf(100, 80, 60, 70, 60, 75, 85)
    val arr = intArrayOf(100, 88, 100, 100, 100, 75, 85)
    val stockSpanner = StockSpanner(arr)
    val res = stockSpanner.getSpannerArray()
    for (i in res.indices) {
        print(res[i].toString() + " ")
    }
}
