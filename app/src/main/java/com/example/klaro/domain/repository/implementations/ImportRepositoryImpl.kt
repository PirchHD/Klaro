package com.example.klaro.domain.repository.implementations

import android.content.Context
import com.example.klaro.domain.model.flashcard.Card
import com.example.klaro.domain.model.flashcard.FlashcardSet
import com.example.klaro.domain.model.ModelCollections
import com.example.klaro.domain.repository.interfaces.ImportRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import javax.inject.Inject

class ImportRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val context: Context
) : ImportRepository
{

    /**
     * Importuj zestaw premium z pliku .xlsx wskazanego przez
     * Path może być np. ścieżką do pliku w assets lub do pliku na dysku.
     */
    fun importPremiumSetFromAssets(assetName: String)
    {
        context.assets.open(assetName).use   { fis ->
            val wb    = XSSFWorkbook(fis)
            val sheet = wb.getSheetAt(0)

            val headerRow = sheet.getRow(1)
            val flashcardSet = parseHeaderRow(headerRow)

            val setRef = firestore.collection(ModelCollections.FLASHCARD_SETS).document(flashcardSet.id)
            setRef.set(flashcardSet)

            val cardsStart = findCardsHeaderRow(sheet) + 1
            val cards = parseCards(sheet, cardsStart)

            val batch = firestore.batch()
            cards.forEach { card ->
                if (card.id?.isBlank() == true) return@forEach

                val cardRef = card.id?.let {
                    setRef
                        .collection(ModelCollections.CARDS)
                        .document(it)
                }

                if (cardRef != null)
                    batch.set(cardRef, card, SetOptions.merge())
            }

            batch.commit()
        }
    }

    /**
     * Z wiersza nagłówka (drugiego w pliku) wydobywa ID dokumentu i mapę pól.
     */
    private fun parseHeaderRow(row: Row): FlashcardSet
    {
        val setId      = row.getCell(0).stringCellValue.trim()
        val title      = row.getCell(1).stringCellValue.trim()
        val description= row.getCell(2)?.stringCellValue?.trim().orEmpty()
        val category   = row.getCell(4)?.stringCellValue?.trim()
        val price      = row.getCell(5)?.numericCellValue
        val imageUrl   = row.getCell(6)?.stringCellValue?.trim().orEmpty()

        return FlashcardSet(
            id         = setId,
            title      = title,
            description= description,
            cards      = emptyList(),
            imageUrl   = imageUrl,
            price      = price,
            category   = category,
            premium    = false,
            ownerId    = ""
        )
    }

    /**
     * Szuka wiersza, w którym w kolumnie A jest dokładnie "CardId".
     * Zwraca jego indeks lub rzuca wyjątkiem, jeśli nie znajdzie.
     */
    private fun findCardsHeaderRow(sheet: Sheet): Int
    {
        for (i in 0 .. sheet.lastRowNum)
        {
            sheet.getRow(i)?.getCell(0)
                ?.takeIf { it.cellType == CellType.STRING }
                ?.stringCellValue
                ?.trim()
                ?.let { if (it == "CardId") return i }
        }

        throw IllegalStateException("Nie znaleziono nagłówka fiszek")
    }

    /**
     * Od [startRow] w dół zbiera front/back fiszek aż do pustego wiersza.
     * Zwraca listę map gotowych do wrzucenia do Firestore.
     */
    private fun parseCards(sheet: Sheet, startRow: Int): List<Card>
    {
        val cards = mutableListOf<Card>()
        var rowIndex = startRow
        var lp = 1

        while (rowIndex <= sheet.lastRowNum)
        {
            val row = sheet.getRow(rowIndex) ?: break

            val cardIdCell = row.getCell(0)
            val frontCell = row.getCell(3)
            val backCell  = row.getCell(4)

            if (frontCell?.cellType != CellType.STRING || backCell?.cellType != CellType.STRING)
                break

            val cardIdText = cardIdCell.stringCellValue.trim()
            val frontText = frontCell.stringCellValue.trim()
            val backText  = backCell.stringCellValue.trim()

            if (frontText.isEmpty() || backText.isEmpty())
                break

            val categoryFront = row.getCell(1)?.stringCellValue?.trim().orEmpty()
            val categoryBack  = row.getCell(2)?.stringCellValue?.trim().orEmpty()

            val exampleOneFront  = row.getCell(5)?.stringCellValue?.trim().orEmpty()
            val exampleOneBack  = row.getCell(6)?.stringCellValue?.trim().orEmpty()

            cards += Card(
                id            = cardIdText,
                lp            = lp,
                front         = frontText,
                back          = backText,
                categoryFront = categoryFront,
                categoryBack  = categoryBack,
                exampleOneFront = exampleOneFront,
                exampleOneBack = exampleOneBack)

            lp++
            rowIndex++
        }

        return cards
    }


}