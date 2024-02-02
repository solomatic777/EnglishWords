package tw.tonyyang.englishwords.data.excel.local

import android.content.Context
import android.net.Uri
import jxl.Sheet
import jxl.Workbook
import tw.tonyyang.englishwords.App
import tw.tonyyang.englishwords.data.excel.ExcelDataSource
import tw.tonyyang.englishwords.database.entity.Word
import java.io.IOException

class ExcelLocalDataSource : ExcelDataSource {
    override fun getData(url: String): List<Word> {
        val workbook = getWorkbook(url)
        val wordList = workbook.sheets.flatMap { sheet ->
            sheet.parseToWordList()
        }
        workbook.close()
        return wordList
    }

    private fun getWorkbook(url: String): Workbook {
        val bytes = getBytes(url)
        App.appContext.openFileOutput(TMP_FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(bytes)
        }
        App.appContext.openFileInput(TMP_FILE_NAME).use {
            return Workbook.getWorkbook(it)
        }
    }

    private fun Sheet.parseToWordList(): List<Word> {
        val wordList = mutableListOf<Word>()
        for (i in 0 until rows) {
            if (getCell(0, i).contents[0].toString() == "#") continue
            wordList.add(
                Word(
                    word = getCell(0, i).contents,
                    wordMean = getCell(1, i).contents,
                    category = getCell(2, i).contents,
                    wordStar = getCell(3, i).contents,
                    wordSentence = getCell(4, i).contents
                )
            )
        }
        return wordList
    }

    private fun getBytes(url: String): ByteArray {
        val uri = Uri.parse(url)
        App.appContext.contentResolver.openInputStream(uri)?.use {
            val bytes = it.readBytes()
            if (bytes.isEmpty()) {
                throw IOException("Local data is empty.")
            }
            return bytes
        }
        throw IOException("Cannot get data from local data source.")
    }

    companion object {
        private const val TMP_FILE_NAME = "temp_vocabulary.xls"
    }
}