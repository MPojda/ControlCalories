import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_first.*

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.SecondActivity)

        // Przypisanie obsługi kliknięcia przycisku
        button1.setBackgroundColor(Color.GREEN) // Domyślny kolor tła przycisku w tej aktywności
        button.setOnClickListener {
            button.setBackgroundColor(Color.BLUE) // Zmiana koloru tła przycisku po kliknięciu
        }
    }
}