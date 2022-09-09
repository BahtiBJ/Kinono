package com.bbj.kinono.view.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageButton
import android.widget.NumberPicker
import androidx.core.view.get
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.bbj.kinono.R

class FilterDialogFragment(private val onFilterChanged: OnFilterChanged) : DialogFragment() {

    private val TAG = "FILTERDIALOG"

    private var genreId = 0
    private var ratingFrom = 0
    private var ratingTo = 0
    private var yearFrom = 0
    private var yearTo = 0

    companion object {
        val genresList = listOf<String>(
            "",
            "триллер",
            "драма",
            "криминал",
            "мелодрама",
            "детектив",
            "фантастика",
            "приключения",
            "биография",
            "фильм-нуар",
            "вестерн",
            "боевик",
            "фэнтези",
            "комедия",
            "военный",
            "история",
            "музыка",
            "ужасы",
            "мультфильм",
            "семейный",
            "мюзикл",
            "спорт",
            "документальный",
            "короткометражка",
            "аниме"
        )
    }

    interface OnFilterChanged {
        fun onFilterChange(genreId: Int, ratingFrom: Int, ratingTo: Int, yearFrom: Int, yearTo: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val closeButton = view.findViewById<ImageButton>(R.id.filter_close_button)
        closeButton.setOnClickListener {
            dismiss()
        }

        val genrePicker = view.findViewById<NumberPicker>(R.id.filter_genre_picker)
        genrePicker.apply {
            displayedValues = null
            minValue = 0
            maxValue = genresList.size - 1
            displayedValues = genresList.toTypedArray()
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            wrapSelectorWheel = false
            setOnValueChangedListener { numberPicker, oldVal, newVal ->
                Log.d(TAG,"Genre id newVAl = $newVal")
                genreId = newVal
            }
        }

        val ratingFromPicker = view.findViewById<NumberPicker>(R.id.filter_rating_from_picker).apply {
            maxValue = 10
            minValue = 0
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            wrapSelectorWheel = false
            setOnValueChangedListener { numberPicker, oldVal, newVal ->
                Log.d(TAG,"RatingFrom newVAl = $newVal")
                ratingFrom = newVal
            }
        }

        val ratingToPicker = view.findViewById<NumberPicker>(R.id.filter_rating_to_picker).apply {
            maxValue = 10
            minValue = 0
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            wrapSelectorWheel = false
            setOnValueChangedListener { numberPicker, oldVal, newVal ->
                Log.d(TAG,"RatingTo newVAl = $newVal")
                ratingTo = newVal
            }
        }

        val yearFromField = view.findViewById<EditText>(R.id.filter_year_from_edittext).apply {
            doAfterTextChanged {
                yearFrom = it.toString().toInt()
            }
        }
        val yearToField = view.findViewById<EditText>(R.id.filter_year_to_edittext).apply {
            doAfterTextChanged {
                yearTo = it.toString().toInt()
            }
        }

        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        arguments?.getIntegerArrayList(SearchFragment.FILTER_KEY).let {
            genreId = it?.get(0) ?: 0
            ratingFrom = it?.get(1) ?: 0
            ratingTo = it?.get(2) ?: 10
            yearFrom = it?.get(3) ?: 1900
            yearTo = it?.get(4) ?: 2100

            genrePicker.value = genreId
            ratingFromPicker.value = ratingFrom
            ratingToPicker.value = ratingTo
            yearFromField.text = Editable.Factory().newEditable(yearFrom.toString())
            yearToField.text = Editable.Factory().newEditable(yearTo.toString())
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        onFilterChanged.onFilterChange(genreId, ratingFrom, ratingTo, yearFrom, yearTo)
        super.onDismiss(dialog)
    }

}