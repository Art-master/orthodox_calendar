package com.artmaster.android.orthodoxcalendar.data.font

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

import com.artmaster.android.orthodoxcalendar.R

import java.util.ArrayList

/**
 * Class for justify text
 * Так же есть возможность установить построчные отступы
 * и указать кол-во символов для отступа
 */
class JustifiedTextView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
    : View(context, attrs, defStyleAttr, defStyleRes) {

    private var mTextPaint: Paint

    lateinit var mText: String

    /** list with info about words (position)  */
    private lateinit var mTextBlocksDrawable: ArrayList<TextBlockDrawable>

    lateinit var mTextColor: ColorStateList

    lateinit var widths: FloatArray
    internal var minSymWidth: Float = 0.toFloat()


    private var fontDescent: Int = 0
    internal var fontInterline: Int = 0
    internal var fontLineHeight: Int = 0
    internal var mLinesCount: Int = 0
    internal var mNumLeadingLines: Int = 0
    internal var mNumSymbolPadding: Int = 0
    internal var mWidth: Int = 0
    private var mTextSize: Int = 0
    private var mCurTextColor: Int = 0
    private var w: Int = 0
    private var h: Int = 0

    private var mLineBreaker: LineBreaker

    init {
        CustomFont.setCustomFont(this, context, attrs,
                R.styleable.customizableView,
                R.styleable.customizableView_customFont)

        mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        //final Resources.Theme theme = context.getTheme();
        val text = ""
        val textSize = 40

        setTextColor(ColorStateList.valueOf(-0x1000000))
        //setTextColor(ColorStateList.valueOf(defStyleAttr);
        setRawTextSize(textSize)
        setText(text)

        mLineBreaker = LineBreaker()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (textBlockDrawable in mTextBlocksDrawable) {
            canvas.drawText(mText, textBlockDrawable.start, textBlockDrawable.end,
                    textBlockDrawable.x.toFloat(), textBlockDrawable.y.toFloat(), mTextPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (w != widthSize) {
            w = widthSize
            mLineBreaker.buildTextBlocks()
        }

        h = when {
            heightMode == MeasureSpec.EXACTLY -> heightSize
            mText.isEmpty() -> fontInterline
            else -> mLinesCount * fontInterline + fontDescent
        }

        setMeasuredDimension(w, h)
    }

    /**
     * Внутренний класс, которые выстраивает символы текста по строкам и рассчитывает пробелы между ними
     */
    private inner class LineBreaker {
        internal var x: Int = 0
        internal var y: Int = 0

        internal var symbolPadding: Int = 0

        /** координаты  */
        internal var posLenStart: Int = 0

        /** кол-во пробелов в строке  */
        internal var spacesLen: Int = 0

        internal var posEOL: Int = 0

        /** кол-во символов в тексте */
        internal var len: Int = 0

        /** массив слов */
        internal lateinit var words: ArrayList<TextBlockDrawable>

        /** обьект для хранения слова. Хранит начальную, конечную позицию */
        internal lateinit var textBlockDrawable: TextBlockDrawable

        fun buildTextBlocks() {
            init()
            if (len == 0) return

            if ((mNumSymbolPadding != 0) and (mLinesCount < mNumLeadingLines)) {
                symbolPadding += (minSymWidth * mNumSymbolPadding).toInt()
                x = symbolPadding
            }
            initNewWord(0)

            //запускаем цикл, в котором перебираем все символы
            var position = 0
            while (position < len) {

                //если символ равен пробелу
                if (mText[position] == ' ') {
                    //+ одну символьную ширину
                    spacesLen += minSymWidth.toInt()

                    //завершаем слово
                    finishWord(position)

                    //заносим это слово в массив слов
                    words.add(textBlockDrawable)

                    //перебираем символы, пока символы равны пробелу
                    while (mText[++position] == ' ') {
                        spacesLen += minSymWidth.toInt()
                    }
                    //начинаем новое слово с текущей позиции
                    initNewWord(position)
                }
                //если попадается символ \n, то начинаем с новой строки, а отступы предыдущей не меняем
                if (mText[position].toInt() == 0x0A) {
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
                if ((mNumSymbolPadding != 0) and (mLinesCount < mNumLeadingLines)) {
                    mWidth = w - symbolPadding
                } else {
                    mWidth = w
                }
                //если позиция выходит за пределы экрана, то ищем место с предыдущим пробелом
                if (x.toFloat() + widths[position] + spacesLen.toFloat() > w) {

                    posEOL = position
                    //просматриваем символы в обратном порядке, пока не натолкнемся на пробел
                    do {
                        //если нашли пробел, то выходим из цикла
                        if (mText[position] == ' ') {
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
            len = mText.length
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
            if ((mNumSymbolPadding > 0) and (mLinesCount < mNumLeadingLines - 1)) {
                x = symbolPadding
            } else {
                x = 0
            }
            y += fontInterline
            mLinesCount++
        }

        /**
         * Распределяем символы между словами данной строки. Для выравнивания по ширине
         * @param backStep true- если необходимо оставить пробелы как есть. Без распределения по ширине
         */
        private fun redistributeSpaces(backStep: Boolean) {
            var widthTotal: Int
            if ((mNumSymbolPadding > 0) and (mLinesCount < mNumLeadingLines)) {
                widthTotal = mWidth
            } else {
                widthTotal = w
            }

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

    fun setTextColor(colors: ColorStateList) {
        mTextColor = colors

        val color = mTextColor.getColorForState(drawableState, 0)
        if (color != mCurTextColor) {
            mCurTextColor = color
            mTextPaint.color = mCurTextColor
        }
    }

    /** set start text size */
    private fun setRawTextSize(size: Int) {
        if (size.toFloat() != mTextPaint.textSize) {
            mTextSize = size
            mTextPaint.textSize = size.toFloat()

            val fm = mTextPaint.fontMetricsInt
            fontDescent = fm.descent
            fontInterline = fm.descent - fm.ascent
            fontLineHeight = -fm.top

            val widths = FloatArray(1)
            mTextPaint.getTextWidths(" ", widths)
            minSymWidth = widths[0]
        }
    }

    fun setText(text: CharSequence) {
        mText = text.toString()
        mTextBlocksDrawable = ArrayList()

        //get chars length
        widths = FloatArray(mText.length)
        mTextPaint.getTextWidths(mText, widths)

        if (w == 0) return

        mLineBreaker.buildTextBlocks()
        invalidate()
    }

    fun setTextSize(unit: Int, size: Float) {
        val c = context
        val r: Resources

        r = if (c == null) Resources.getSystem() else c.resources

        setRawTextSize(TypedValue.applyDimension(
                unit, size, r.displayMetrics).toInt())
        invalidate()
    }

    /** safe info about words in text */
    private class TextBlockDrawable internal constructor(
            internal var x: Int,
            internal var y: Int,
            internal var start: Int,
            internal var end: Int = 0
            )

    /** set custom font */
    fun setTypeface(tf: Typeface) {
        if (mTextPaint.typeface !== tf) {
            mTextPaint.typeface = tf
            requestLayout()
            invalidate()
        }
    }

    /**
     * set start padding
     * @param lineCount num lines with the padding
     * @param symPadding num symbols for padding
     */
    fun setLeadingMargin(lineCount: Int, symPadding: Int) {
        mNumLeadingLines = lineCount
        mNumSymbolPadding = symPadding
    }
}
