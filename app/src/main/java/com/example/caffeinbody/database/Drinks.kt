package com.example.caffeinbody.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Drinks(
    var drinkName: String,
    var size: Int,
    var caffeine: Int,

    var category: String,
    var madeBy: String,
    var imgurl: String,
 //   var isCoffee: Boolean
 // iscoffee는 카페 메뉴 중에 커피 아닌 메뉴(밀크티, 녹차라떼 같은) +일반음료
    // -> 카페인 샷이 아닌 용량별로 계산할 수 있는 메뉴 음료
    //   var caffeine: List<Int>,
    var iscafe: Boolean

){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
