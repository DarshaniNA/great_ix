package com.beardedhen.great_ix.export

import android.content.Context
import com.beardedhen.great_ix.R
import com.beardedhen.great_ix.db.FormEntity
import java.io.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class FileManager {

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("dd_MM_yyyy_hh_mm_ss")


        fun buildEmailFilename( context: Context, formEntity: FormEntity ) : String {

            // TODO - AK - 10/04/20- replace any special characters that might cause issues with file names

            val pdfFilename = StringBuilder()

            val placeOfWork = when( formEntity.yourPlaceOfWork ) {
                "Other" -> formEntity.yourOtherPlaceOfWork
                else -> formEntity.yourPlaceOfWork
            }

            pdfFilename.append( context.resources.getString(formEntity.thankYouCard?.cardNameResId ?: R.string.no_card_selected))
            pdfFilename.append( " - " )
            pdfFilename.append( placeOfWork.replace(" ", "_") )
            pdfFilename.append( " - " )
            pdfFilename.append( formEntity.yourName.replace(" ", "_") )
            pdfFilename.append( " - " )
            pdfFilename.append( formEntity.whoWasGreat.replace(" ", "_") )

            return pdfFilename.toString()
        }

        @Throws(IOException::class)
        fun createCSVExportFile(context: Context, formEntity: FormEntity): File {

            val fileName = buildEmailFilename( context, formEntity) + ".csv"

            val myPath = File( context.filesDir,fileName);
            myPath.writeText( formEntity.toCSV(context))

            return myPath
        }

        @Throws(IOException::class)
        private fun saveToFile(inputStream: InputStream, outputFile: File?) {

            inputStream.use { inputStream ->

                val output: OutputStream = FileOutputStream(outputFile)

                output.use { output ->

                    val buffer = ByteArray(4 * 1024)
                    var read: Int
                    while (inputStream.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                    output.flush()
                }
            }
        }
    }

}