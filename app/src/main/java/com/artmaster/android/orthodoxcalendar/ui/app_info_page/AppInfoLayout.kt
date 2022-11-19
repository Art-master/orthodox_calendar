package com.artmaster.android.orthodoxcalendar.ui.app_info_page

import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.common.DividerWithText
import com.artmaster.android.orthodoxcalendar.ui.common.Header
import com.artmaster.android.orthodoxcalendar.ui.common.StyledText
import com.artmaster.android.orthodoxcalendar.ui.theme.*

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
fun AppInfoLayoutPreview() {
    AppInfoLayout()
}

private val TILE_SIZE = 40.dp
private val ICON_SIZE = 30.dp

@Composable
fun AppInfoLayout() {
    val scroll = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(scroll)) {
        Header(title = stringResource(id = R.string.сonvention))

        DividerWithText(text = stringResource(id = R.string.colors))

        Element(title = stringResource(id = R.string.great_easter_name)) {
            Tile(text = "1", color = Easter, fontColor = Color.White)
        }
        Element(title = stringResource(id = R.string.great_twelve_name)) {
            Tile(text = "2", color = TwelveHoliday, fontColor = Color.White)
        }
        Element(title = stringResource(id = R.string.great_holidays_name)) {
            Tile(text = "3", color = HeadHoliday, fontColor = Color.White)
        }
        Element(title = stringResource(id = R.string.fasting_day)) {
            Tile(text = "4", color = FastingDay, fontColor = Color.Black)
        }
        Element(title = stringResource(id = R.string.solid_week)) {
            Tile(text = "5", color = DayOfSolidWeek, fontColor = Color.Black)
        }

        DividerWithText(text = stringResource(id = R.string.images))

        Element(title = stringResource(id = R.string.fish_allow)) {
            HelpImage(imageId = R.drawable.ic_tile_fish)
        }
        Element(title = stringResource(id = R.string.oil_allow)) {
            HelpImage(imageId = R.drawable.ic_tile_sun)
        }
        Element(title = stringResource(id = R.string.eggs_allow)) {
            HelpImage(imageId = R.drawable.ic_tile_eggs)
        }
        Element(title = stringResource(id = R.string.remember_day)) {
            HelpImage(imageId = R.drawable.cross)
        }
        Element(title = stringResource(id = R.string.strict_fasting)) {
            HelpImage(imageId = R.drawable.ic_tile_triangle)
        }
        Element(title = stringResource(id = R.string.filter_birthdays)) {
            HelpFontImage(strFontId = R.string.birthday, color = Birthday)
        }
        Element(title = stringResource(id = R.string.filter_name_days)) {
            HelpFontImage(strFontId = R.string.name_day, color = NameDay)
        }

        DividerWithText(text = stringResource(id = R.string.contacts))

        Contacts()
    }
}

@Composable
fun HelpImage(imageId: Int) {
    Image(
        modifier = Modifier
            .size(ICON_SIZE)
            .rotate(0f)
            .fillMaxSize(0.25f),
        painter = painterResource(id = imageId),
        contentDescription = ""
    )
}

@Composable
fun HelpFontImage(strFontId: Int, color: Color) {
    Text(
        modifier = Modifier.size(ICON_SIZE),
        color = color,
        text = stringResource(id = strFontId),
        fontSize = 40.sp,
        fontFamily = FontFamily(Font(R.font.ort_basic, FontWeight.Normal)),
        textAlign = TextAlign.Center
    )
}

@Composable
fun Element(title: String, image: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .padding(start = 5.dp, top = 3.dp)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        image()
        StyledText(modifier = Modifier.padding(start = 10.dp), title = title)
    }
}

@Composable
fun Tile(text: String, color: Color, fontColor: Color) {
    Box(
        Modifier
            .size(TILE_SIZE)
            .aspectRatio(1f)
            .background(color, shape = RoundedCornerShape(6.dp))
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(6.dp)
            )
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 2.dp, top = 1.dp),
            text = text,
            fontSize = with(LocalDensity.current) {
                (20 / fontScale).sp
            },
            color = fontColor,
            fontFamily = FontFamily(Font(R.font.ort_basic)),
            textAlign = TextAlign.Left
        )
    }
}


@Composable
fun Contacts() {
    val ctx = LocalContext.current

    val text = stringResource(id = R.string.app_contacts)
    val email = stringResource(id = R.string.app_email)
    val emailSubject = stringResource(id = R.string.app_email_subject)

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 2.dp, top = 1.dp),
        text = text,
        fontSize = with(LocalDensity.current) {
            (20 / fontScale).sp
        },
        color = DefaultTextColor,
        fontFamily = FontFamily(Font(R.font.cyrillic_old)),
        textAlign = TextAlign.Left
    )


    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 2.dp, top = 2.dp, bottom = 20.dp)
            .clickable {
                val i = Intent(Intent.ACTION_SEND)
                val emailAddress = arrayOf(email)
                i.putExtra(Intent.EXTRA_EMAIL, emailAddress)
                i.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                i.type = "message/rfc822"

                ctx.startActivity(Intent.createChooser(i, ""))


            },
        text = email,
        fontSize = with(LocalDensity.current) {
            (20 / fontScale).sp
        },
        color = LinksColor,
        fontFamily = FontFamily(Font(R.font.decorated)),
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline
    )
}