package tw.tonyyang.englishwords.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tw.tonyyang.englishwords.App
import tw.tonyyang.englishwords.R
import tw.tonyyang.englishwords.data.excel.local.ExcelLocalDataSource
import tw.tonyyang.englishwords.database.entity.Word
import java.lang.IllegalArgumentException


class ExcelRepository(
    private val localDataSource: ExcelLocalDataSource
) {
    suspend fun getWordList(fileUrl: String?): Flow<List<Word>> = flow {
        if (fileUrl.isNullOrBlank()) {
            throw IllegalArgumentException(App.appContext.getString(R.string.import_excel_failed))
        }
        val workbook = localDataSource.getData(fileUrl)
        emit(workbook)
    }
}