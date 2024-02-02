package tw.tonyyang.englishwords.data.excel

import tw.tonyyang.englishwords.database.entity.Word

interface ExcelDataSource {
    fun getData(url: String): List<Word>
}