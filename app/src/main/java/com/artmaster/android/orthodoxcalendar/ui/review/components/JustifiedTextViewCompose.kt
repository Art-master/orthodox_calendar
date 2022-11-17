package com.artmaster.android.orthodoxcalendar.ui.review.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.FontRes
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.res.ResourcesCompat

/**
 * Class for justify text
 * Так же есть возможность установить построчные отступы
 * и указать кол-во символов для отступа
 */
class JustifiedTextViewCompose(private val context: Context) {

    private var textPaint: Paint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    lateinit var text: String

    /** list with info about words (position)  */
    private lateinit var mTextBlocksDrawable: ArrayList<TextBlockDrawable>

    lateinit var widths: FloatArray
    internal var minSymWidth: Float = 0f


    private var fontDescent: Int = 0
    internal var fontInterline: Int = 0
    internal var fontLineHeight: Int = 0
    internal var mLinesCount: Int = 0
    internal var numLeadingLines: Int = 0
    internal var numSymbolPadding: Int = 0
    internal var mWidth: Int = 0
    private var textSize: Int = 0
    private var w: Int = 0
    private var h: Int = 0

    private var lineBreaker = LineBreaker()

    init {
        val textSize = 40
        setRawTextSize(textSize)
        setText("")
    }

    fun onDraw(canvas: Canvas) {

        for (textBlockDrawable in mTextBlocksDrawable) {
            canvas.drawText(
                text, textBlockDrawable.start, textBlockDrawable.end,
                textBlockDrawable.x.toFloat(), textBlockDrawable.y.toFloat(), textPaint
            )
        }
    }

    fun setWidth(width: Int) {
        w = width
    }

    fun getCalculatedHeight(): Int {
        //assert(text.isNotEmpty() && mTextBlocksDrawable.isNotEmpty())
        if (mTextBlocksDrawable.isEmpty()) return 0
        return mTextBlocksDrawable.last().y + 10
    }

    fun setCanvasSize(size: Size) {
        h = size.height.toInt()
        w = size.width.toInt()
    }

    /**
     * Внутренний класс, которые выстраивает символы текста по строкам и рассчитывает пробелы между ними
     */
    private inner class LineBreaker {
        var x: Int = 0
        var y: Int = 0

        var symbolPadding: Int = 0

        /** координаты  */
        var posLenStart: Int = 0

        /** кол-во пробелов в строке  */
        var spacesLen: Int = 0

        var posEOL: Int = 0

        /** кол-во символов в тексте */
        var len: Int = 0

        /** массив слов */
        lateinit var words: ArrayList<TextBlockDrawable>

        /** обьект для хранения слова. Хранит начальную, конечную позицию */
        lateinit var textBlockDrawable: TextBlockDrawable

        fun buildTextBlocks() {
            init()
            if (len == 0) return

            if ((numSymbolPadding != 0) and (mLinesCount < numLeadingLines)) {
                symbolPadding += (minSymWidth * numSymbolPadding).toInt()
                x = symbolPadding
            }
            initNewWord(0)

            //запускаем цикл, в котором перебираем все символы
            var position = 0
            while (position < len) {

                //если символ равен пробелу
                if (text[position] == ' ') {
                    //+ одну символьную ширину
                    spacesLen += minSymWidth.toInt()

                    //завершаем слово
                    finishWord(position)

                    //заносим это слово в массив слов
                    words.add(textBlockDrawable)

                    //перебираем символы, пока символы равны пробелу
                    while (text[++position] == ' ') {
                        spacesLen += minSymWidth.toInt()
                    }
                    //начинаем новое слово с текущей позиции
                    initNewWord(position)
                }
                //если попадается символ \n, то начинаем с новой строки, а отступы предыдущей не меняем
                if (text[position].code == 0x0A) {
                    //распределяем пробелы
                    redistributeSpaces(true)
                    //переходим на новую строку и обновляем переменные
                    newLineResetValues()
                    //сохраняем координаты
                    textBlockDrawable.x = x
                    textBlockDrawable.y = y + fontLineHeight
                    posLenStart = position
                    position++
                    continue
                }

                //если курсор нодошел к последнему символу текста
                if (position == len - 1) {
                    //завершаем слово
                    finishWord(position + 1)
                    //заносим это слово в массив слов
                    words.add(textBlockDrawable)
                    //сохраняем последнее слово
                    initNewWord(position + 1)
                    //распределяем пробелы
                    redistributeSpaces(true)
                    //сохраняем координаты
                    textBlockDrawable.x = x
                    textBlockDrawable.y = y + fontLineHeight
                    posLenStart = position + 1
                    position++
                    continue
                }

                //если заданы отступы, вычисляем ширину рабочей области с учетом отступов
                mWidth = if ((numSymbolPadding != 0) and (mLinesCount < numLeadingLines)) {
                    w - symbolPadding
                } else w

                //если позиция выходит за пределы экрана, то ищем место с предыдущим пробелом
                if (x.toFloat() + widths[position] + spacesLen.toFloat() > w) {

                    posEOL = position
                    //просматриваем символы в обратном порядке, пока не натолкнемся на пробел
                    do {
                        //если нашли пробел, то выходим из цикла
                        if (text[position] == ' ') {
                            position++
                            break
                        }
                        //цикл выполняется, пока мы не дойдем до начала строки, с которой начали.
                    } while (--position > posLenStart)


                    //Если одно слово не вписывается в строку
                    if (position == posLenStart) {
                        position = posEOL
                        //переходим на новую строку и обновляем переменные
                        newLineResetValues()
                        //завершаем слово
                        finishWord(position)
                        //инициализируем новое слово
                        initNewWord(position)

                        posLenStart = position
                        //к коорд. х прибавляем ширину текущего символа
                        x += widths[position].toInt()
                        position++
                        continue
                    } else if (position < 0) {
                        //Если не подходит даже 1 символ
                        return
                    }
                    //распределяем пробелы
                    redistributeSpaces(false)
                    //переходим на новую строку и обновляем переменные
                    newLineResetValues()
                    //сохраняем координаты
                    textBlockDrawable.x = x
                    textBlockDrawable.y = y + fontLineHeight
                    posLenStart = position
                }
                x += widths[position].toInt()
                position++
            }
            textBlockDrawable.end = len
            mTextBlocksDrawable.add(textBlockDrawable)
            mLinesCount++
        }

        private fun init() {
            y = 0
            x = 0
            posLenStart = 0
            spacesLen = 0
            mLinesCount = 0
            words = ArrayList()
            len = text.length
            mTextBlocksDrawable.clear()
        }

        private fun initNewWord(pos: Int) {
            textBlockDrawable = TextBlockDrawable(x, y + fontLineHeight, pos)
        }

        /**
         * Boundary of world
         * @param pos - position of symbol in text
         */
        private fun finishWord(pos: Int) {
            textBlockDrawable.end = pos
            mTextBlocksDrawable.add(textBlockDrawable)
        }

        /**
         * Переходна новую строку, и обновление переменных
         */
        private fun newLineResetValues() {
            spacesLen = 0

            //смотрим, если заданы отступы, устанавниваем значение х, равное этому расстоянию
            x = if ((numSymbolPadding > 0) and (mLinesCount < numLeadingLines - 1)) {
                symbolPadding
            } else {
                0
            }
            y += fontInterline
            mLinesCount++
        }

        /**
         * Распределяем символы между словами данной строки. Для выравнивания по ширине
         * @param backStep true- если необходимо оставить пробелы как есть. Без распределения по ширине
         */
        private fun redistributeSpaces(backStep: Boolean) {
            var widthTotal = if ((numSymbolPadding > 0) and (mLinesCount < numLeadingLines)) {
                mWidth
            } else w

            if (words.size <= 1) {
                words.clear()
                return
            }

            //Перебираем массив слов и вычисляем из ширины экрана длинны слов, вычисляя тем самым
            //общее кол-во пробелов
            for (wrd in words) {
                for (i in wrd.start until wrd.end) {
                    widthTotal -= widths[i].toInt()
                }
            }

            //число слов в строке
            val wordsCount = words.size - 1
            //Длина пробела в строке перед каждым словом
            val spaceLen = widthTotal / wordsCount
            //Остаток пробелов
            var spacesMod = widthTotal % wordsCount
            //Сдвиг пробелов
            var spacesShift = 0
            //Перебираем массив слов и прибавляем к каждому слову вычисленное число пробелов

            for (word in words) {
                if (backStep) {
                    word.x += spacesShift
                    spacesShift += minSymWidth.toInt()
                } else {
                    word.x += spacesShift
                    spacesShift += spaceLen

                    if (spacesMod-- > 0) {
                        spacesShift++
                    }
                }

            }

            words.clear()
        }
    }

    fun setTextColor(color: Color) {
        textPaint.color = color.toArgb()
    }

    /** set start text size */
    fun setRawTextSize(size: Int) {
        if (size.toFloat() != textPaint.textSize) {
            textSize = size
            textPaint.textSize = size.toFloat()

            val fm = textPaint.fontMetricsInt
            fontDescent = fm.descent
            fontInterline = fm.descent - fm.ascent
            fontLineHeight = -fm.top

            val widths = FloatArray(1)
            textPaint.getTextWidths(" ", widths)
            minSymWidth = widths[0]
        }
    }

    fun setText(text: CharSequence) {
        this.text = text.toString()
        mTextBlocksDrawable = ArrayList()

        //get chars length
        widths = FloatArray(this.text.length)
        textPaint.getTextWidths(this.text, widths)

    }

    fun calculate() {
        lineBreaker.buildTextBlocks()
    }

    fun setTextSize(unit: Int, size: Float, metrics: DisplayMetrics) {
        setRawTextSize(TypedValue.applyDimension(unit, size, metrics).toInt())
    }

    /** safe info about words in text */
    private class TextBlockDrawable constructor(
        var x: Int,
        var y: Int,
        var start: Int,
        var end: Int = 0
    )

    /** set custom font */
    fun setTypeface(tf: Typeface) {
        if (textPaint.typeface !== tf) {
            textPaint.typeface = tf
        }
    }

    fun setFont(@FontRes id: Int) {
        val typeface = ResourcesCompat.getFont(context, id)
        setTypeface(typeface!!)
    }

    /**
     * set start padding
     * @param lineCount num lines with the padding
     * @param symPadding num symbols for padding
     */
    fun setLeadingMargin(lineCount: Int, symPadding: Int) {
        numLeadingLines = lineCount
        numSymbolPadding = symPadding
    }
}
