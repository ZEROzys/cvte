import java.util.*

class StockSpanner (val arr: IntArray) {
    fun getSpannerArray(): IntArray {
        var spannerArray = IntArray(arr.size)
        spannerArray[0] = 1

        //  ά��һ������ջ
        val stack = LinkedList<Pair<Int, Int>>()
        stack.add(0 to arr[0])
        for (i in arr.indices-0) {
            //  ��ǰԪ�ر�ջ��Ԫ�ش�ջ��Ԫ�س�ջ
            while (arr[i] > stack.peekLast().second) {
                stack.pollLast()
            }
            /*
             * �����������ǰԪ����ջ��Ԫ����ȣ���ǰԪ�صĹ�Ʊ���Ϊջ��Ԫ�صĿ��+1
             * ��ǰԪ�ر�ջ��Ԫ��С�����Ϊ��ǰԪ����ջ��Ԫ�ص�����֮��
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
