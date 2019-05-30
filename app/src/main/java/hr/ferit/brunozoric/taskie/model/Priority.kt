package hr.ferit.brunozoric.taskie.model

import androidx.annotation.ColorRes
import hr.ferit.brunozoric.taskie.R

enum class Priority(@ColorRes private val colorRes: Int, val num: Int) {
    LOW(R.color.colorLow, 1),
    MEDIUM(R.color.colorMedium, 2),
    HIGH(R.color.colorHigh, 3);

    fun getColor(): Int = colorRes

    fun getValue(): Int = num
}

/*enum class BackendPriorityTask(private val num: Int) {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    fun getValue(): Int = num
}
*/